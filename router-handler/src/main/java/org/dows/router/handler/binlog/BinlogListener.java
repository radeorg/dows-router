package org.dows.router.handler.binlog;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Binlog 监听器
 * 模拟 binlog 监听功能，实际项目中可以使用 Canal、Maxwell 等工具
 *
 * @author lait.zhang@gmail.com
 * @since 1.0.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class BinlogListener {

    private final BinlogConsumer binlogConsumer;

    private final BinlogEventProcessor binlogEventProcessor;

    // 模拟 binlog 监听线程
    private ExecutorService binlogListenerExecutor;
    private volatile boolean running = false;

    @PostConstruct
    public void init() {
        log.info("初始化 Binlog 监听器...");

        binlogListenerExecutor = Executors.newSingleThreadExecutor(
                r -> new Thread(r, "binlog-listener"));

        running = true;
        binlogListenerExecutor.submit(this::startBinlogListener);

        log.info("Binlog 监听器初始化完成");
    }

    @PreDestroy
    public void destroy() {
        log.info("关闭 Binlog 监听器...");
        running = false;

        if (binlogListenerExecutor != null) {
            binlogListenerExecutor.shutdown();
            try {
                if (!binlogListenerExecutor.awaitTermination(30, TimeUnit.SECONDS)) {
                    log.warn("Binlog 监听器未能在30秒内正常关闭，强制关闭");
                    binlogListenerExecutor.shutdownNow();
                }
            } catch (InterruptedException e) {
                log.error("等待 Binlog 监听器关闭时被中断", e);
                binlogListenerExecutor.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }

        log.info("Binlog 监听器已关闭");
    }

    /**
     * 启动 binlog 监听
     * 实际项目中这里会连接到 MySQL binlog 或使用 Canal 等工具
     */
    private void startBinlogListener() {
        log.info("Binlog 监听线程启动: {}", Thread.currentThread().getName());

        while (running) {
            try {
                // 模拟监听 binlog 事件
                // 实际项目中这里会从 Canal、Maxwell 或直接从 MySQL binlog 读取事件
                simulateBinlogEvent();

                // 模拟监听间隔
                Thread.sleep(1000);

            } catch (InterruptedException e) {
                log.info("Binlog 监听线程被中断: {}", Thread.currentThread().getName());
                Thread.currentThread().interrupt();
                break;
            } catch (Exception e) {
                log.error("Binlog 监听异常", e);
                // 可以在这里实现重连逻辑
            }
        }

        log.info("Binlog 监听线程结束: {}", Thread.currentThread().getName());
    }

    /**
     * 模拟 binlog 事件
     * 实际项目中这个方法会被 Canal 或其他 binlog 工具的回调替代
     */
    private void simulateBinlogEvent() {
        // 这里只是模拟，实际项目中不需要这个方法
        // 真实的 binlog 事件会通过 Canal 等工具推送过来

        // 示例：模拟一个 router_data 表的 INSERT 事件
        if (Math.random() < 0.1) { // 10% 概率模拟事件
            BinlogEvent event = createMockBinlogEvent();
            handleBinlogEvent(event);
        }
    }

    /**
     * 处理 binlog 事件
     * 这个方法会被 Canal 等工具调用
     */
    public void handleBinlogEvent(BinlogEvent event) {
        try {
            log.debug("接收到 binlog 事件: {}", event.getRouterDataId());

            // 标记事件开始处理
            event.markProcessing();

            // 委托给 BinlogConsumer 处理
            binlogConsumer.handleBinlogEvent(event);

            // 标记处理成功
            event.markSuccess();
            binlogEventProcessor.recordProcessSuccess(event);

        } catch (Exception e) {
            // 标记处理失败
            event.markFailed(e.getMessage());
            binlogEventProcessor.recordProcessError(event, e);

            log.error("处理 binlog 事件失败: routerDataId={}", event.getRouterDataId(), e);
        }
    }

    /**
     * 创建模拟的 binlog 事件
     */
    private BinlogEvent createMockBinlogEvent() {
        BinlogEvent event = BinlogEvent.createInsertEvent(
                "router_db",
                "router_data",
                System.currentTimeMillis(),
                "MOCK_APP"
        );

        // 设置模拟数据
        event.setOperatorId(12345L);
        event.setRouterChannelId(67890L);
        event.setSessionType(1);
        event.setPriority(5);
        event.setRequestId("REQ_" + System.currentTimeMillis());
        event.setData("{\"mock\": \"data\"}");
        event.setState((byte) 0); // 待处理状态
        event.setDeleted((byte) 0);

        return event;
    }

    /**
     * 获取监听状态
     */
    public boolean isRunning() {
        return running;
    }

    /**
     * 手动触发 binlog 事件处理（用于测试）
     */
    public void triggerMockBinlogEvent(Long routerDataId, String appId) {
        BinlogEvent event = BinlogEvent.createInsertEvent(
                "router_db",
                "router_data",
                routerDataId,
                appId
        );

        // 设置基本数据
        event.setOperatorId(12345L);
        event.setRouterChannelId(67890L);
        event.setSessionType(1);
        event.setPriority(5);
        event.setRequestId("REQ_" + routerDataId);
        event.setData("{\"test\": \"data\", \"routerDataId\": " + routerDataId + "}");
        event.setState((byte) 0);
        event.setDeleted((byte) 0);

        handleBinlogEvent(event);
        log.info("手动触发 binlog 事件: routerDataId={}", routerDataId);
    }
}