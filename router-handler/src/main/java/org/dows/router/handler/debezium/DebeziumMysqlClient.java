package org.dows.router.handler.debezium;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import io.debezium.engine.ChangeEvent;
import io.debezium.engine.DebeziumEngine;
import io.debezium.engine.format.Json;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dows.router.handler.event.TableEventHandler;
import org.springframework.context.SmartLifecycle;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.dows.router.handler.binlog.BinlogConsumer;
import org.dows.router.handler.binlog.BinlogEvent;
import org.dows.router.handler.binlog.BinlogEventProcessor;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Debezium MySQL 客户端（嵌入式引擎）
 * - 由 Spring 容器生命周期管理，应用启动后自动启动引擎，关闭时优雅停止
 * - 无 main 方法；主线程在应用入口（如 SpringBootApplication）中
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DebeziumMysqlClient implements SmartLifecycle {

    private final Environment env;

    private final List<TableEventHandler> tableEventHandlerList;

    private final BinlogEventProcessor binlogEventProcessor;


    private volatile boolean running = false;
    private DebeziumEngine<ChangeEvent<String, String>> engine;
    private ExecutorService executor;
    private final Map<String, TableEventHandler> tableHandleableMap = new HashMap<>(tableEventHandlerList.size());


    @Override
    public void start() {
        if (running) {
            return;
        }
        try {
            // 配置表处理映射
            tableEventHandlerList.forEach(handleable -> tableHandleableMap.put(handleable.getTableName(), handleable));
            Properties props = configureDebeziumProperties();

            engine = DebeziumEngine
                    .create(Json.class)
                    .using(props)
                    .notifying(this::processRecords)
                    .build();

            executor = Executors.newSingleThreadExecutor(r -> new Thread(r, "debezium-mysql-engine"));
            executor.execute(engine);

            Runtime.getRuntime().addShutdownHook(new Thread(this::shutdownSafely, "debezium-shutdown-hook"));

            running = true;
            log.info("Debezium MySQL 引擎已启动");
        } catch (Exception e) {
            log.error("启动 Debezium 引擎失败: {}", e.getMessage(), e);
        }
    }

    @Override
    public void stop() {
        shutdownSafely();
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    @Override
    public boolean isAutoStartup() {
        // 应用启动后自动启动
        return true;
    }

    @Override
    public int getPhase() {
        // 默认启动/停止顺序
        return 0;
    }

    private void shutdownSafely() {
        if (!running) {
            return;
        }
        try {
            log.info("正在关闭 Debezium 引擎...");
            if (engine != null) {
                engine.close();
            }
            if (executor != null) {
                executor.shutdown();
            }
        } catch (IOException e) {
            log.error("关闭 Debezium 引擎异常: {}", e.getMessage(), e);
        } finally {
            running = false;
            log.info("Debezium 引擎已关闭");
        }
    }

    /**
     * 处理捕获到的变更事件
     */
    private void processRecords(ChangeEvent<String, String> record) {
        String value = record.value();
        if (value == null) {
            return;
        }
        try {
            JSONObject from = JSONUtil.parseObj(value);
            // 获取数据库表名
            JSONObject source = from.getJSONObject("source");
            String db = source != null ? source.getStr("db") : null;
            String table = source != null ? source.getStr("table") : null;
            // r,c,u,d
            String op = from.getStr("op");

            JSONObject before = from.getJSONObject("before");
            JSONObject after = from.getJSONObject("after");
            String ddl = from.getStr("ddl");

            // 操作类型 op: r(读取) c(创建), u(更新), d(删除)
            log.info("++++++++++++++++++++++++ MySQL Binlog Change Event ++++++++++++++++++++++++");
            log.info("db.table.op：{}.{}.{}", db, table, op);
            log.info("change before: {}", (Objects.nonNull(before) ? before.toJSONString(0) : ""));
            log.info("change after: {}", (Objects.nonNull(after) ? after.toJSONString(0) : ""));
            log.info("ddl: {}", (Objects.nonNull(ddl) ? ddl : ""));

            // 处理表变更事件
            TableEventHandler tableEventHandler = tableHandleableMap.get(table);
            if (tableEventHandler == null) {
                log.warn("未找到处理表 {} 的处理器，忽略", table);
                return;
            }
            // 处理变更事件
            tableEventHandler.handle(op , before, after);
        } catch (Exception e) {
            log.error("处理 Debezium 事件异常: {}", e.getMessage(), e);
            // 记录错误
            try {
                BinlogEvent failed = new BinlogEvent();
                failed.setTableName("router_data");
                failed.setDatabase("dev_bole");
                failed.setEventType(BinlogEvent.EventType.INSERT);
                failed.markFailed(e.getMessage());
                binlogEventProcessor.recordProcessError(failed, e);
            } catch (Exception ignore) {
                // 忽略记录错误的异常
            }
        }
    }

    /**
     * 配置 Debezium 连接器属性
     * 可从环境变量/系统属性读取（若存在），否则使用默认值；后续可迁移到 application.yml
     */
    private Properties configureDebeziumProperties() {
        Properties props = new Properties();
        // 基本配置
        props.setProperty("name", getProp("debezium.name", "mysql-connector"));
        props.setProperty("topic.prefix", getProp("debezium.topic.prefix", "router-"));
        props.setProperty("connector.class", "io.debezium.connector.mysql.MySqlConnector");

        // MySQL 连接信息
        props.setProperty("database.hostname", getProp("debezium.mysql.host", "localhost"));
        props.setProperty("database.user", getProp("debezium.mysql.user", "debezium"));
        props.setProperty("database.password", getProp("debezium.mysql.password", "123456"));
        props.setProperty("database.port", getProp("debezium.mysql.port", "13306"));
        props.setProperty("database.server.id", getProp("debezium.mysql.server.id", "184055"));
        props.setProperty("database.server.name", getProp("debezium.mysql.server.name", "mysql-server"));

        // 监听范围
        props.setProperty("database.include.list", getProp("debezium.mysql.database.include", "dev_bole"));
        props.setProperty("table.include.list", getProp("debezium.mysql.table.include", "dev_bole.router_data"));

        // 快照模式
        props.setProperty("snapshot.mode", getProp("debezium.snapshot.mode", "initial"));

        // 偏移量
        props.setProperty("offset.flush.interval.ms", getProp("debezium.offset.flush.interval.ms", "5000"));
        props.setProperty("offset.storage", getProp("debezium.offset.storage", "org.apache.kafka.connect.storage.FileOffsetBackingStore"));
        props.setProperty("offset.storage.file.filename", getProp("debezium.offset.file", "mysql-offset.dat"));

        // Schema 历史
        props.setProperty("include.schema.changes", getProp("debezium.include.schema.changes", "false"));
        props.setProperty("schema.history.internal.store.only.captured.tables.ddl",
                getProp("debezium.schema.store.only.captured.tables.ddl", "true"));
        props.setProperty("schema.history.internal", getProp("debezium.schema.history.internal",
                "io.debezium.storage.file.history.FileSchemaHistory"));
        props.setProperty("schema.history.internal.file.filename",
                getProp("debezium.schema.history.file", "schema-history.dat"));

        // 记录格式（Debezium 3.x）
        props.setProperty("value.converter", getProp("debezium.value.converter", "org.apache.kafka.connect.json.JsonConverter"));
        props.setProperty("value.converter.schemas.enable", getProp("debezium.value.converter.schemas.enable", "false"));

        return props;
    }

    private String getProp(String key, String defaultValue) {
        String val = System.getProperty(key);
        if (val == null) {
            val = env.getProperty(key);
        }
        return val != null ? val : defaultValue;
    }
}