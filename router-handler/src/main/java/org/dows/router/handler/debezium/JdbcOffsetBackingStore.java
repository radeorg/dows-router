package org.dows.router.handler.debezium;

import cn.hutool.core.lang.Snowflake;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.connect.errors.ConnectException;
import org.apache.kafka.connect.runtime.WorkerConfig;
import org.apache.kafka.connect.storage.OffsetBackingStore;
import org.apache.kafka.connect.util.Callback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;


public class JdbcOffsetBackingStore implements OffsetBackingStore {
    private static final Logger log = LoggerFactory.getLogger(JdbcOffsetBackingStore.class);

    //这里使用雪花算法生成表主键Id，可根据实际情况调整
    public static final Snowflake SNOWFLAKE = new Snowflake(1, 1);

    private String tableName;
    private Connection connection;

    @Override
    public void start() {
        log.info("Starting JdbcOffsetBackingStore");
        try {
            // 创建表结构
            createOffsetTableIfNotExists();
        } catch (SQLException e) {
            throw new ConnectException("Failed to start JdbcOffsetBackingStore", e);
        }
    }

    @Override
    public void stop() {
        log.info("Stopping JdbcOffsetBackingStore");
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                log.warn("Error while closing JDBC connection", e);
            }
        }
    }

    @Override
    public Future<Map<ByteBuffer, ByteBuffer>> get(Collection<ByteBuffer> keys) {
        return CompletableFuture.supplyAsync(() -> {
            Map<ByteBuffer, ByteBuffer> result = new HashMap<>();
            for (ByteBuffer key : keys) {
                ByteBuffer value = getOffset(key);
                if (value != null) {
                    result.put(key, value);
                }
            }
            return result;
        });
    }

    @Override
    public Future<Void> set(Map<ByteBuffer, ByteBuffer> values, Callback<Void> callback) {
        return CompletableFuture.runAsync(() -> {
            for (Map.Entry<ByteBuffer, ByteBuffer> entry : values.entrySet()) {
                setOffset(entry.getKey(), entry.getValue());
            }
            if (callback != null) {
                callback.onCompletion(null, null);
            }
        });
    }


    @Override
    public Set<Map<String, Object>> connectorPartitions(String connectorName) {
        return Set.of();
    }

    @Override
    public void configure(WorkerConfig config) {
        Map<String, Object> originals = config.originals();
        String jdbcUrl = (String) originals.getOrDefault(JdbcWorkerConfig.OFFSET_STORAGE_JDBC_URL_CONFIG, "");
        String username = (String) originals.getOrDefault(JdbcWorkerConfig.OFFSET_STORAGE_JDBC_USER_CONFIG, "");
        String password = (String) originals.getOrDefault(JdbcWorkerConfig.OFFSET_STORAGE_JDBC_PASSWORD_CONFIG, "");
        tableName = (String) originals.getOrDefault(JdbcWorkerConfig.OFFSET_STORAGE_JDBC_TABLE_NAME_CONFIG, "");
        try {
            // 建立数据库连接
            connection = DriverManager.getConnection(jdbcUrl, username, password);
        } catch (SQLException e) {
            throw new ConnectException("Failed to configure JdbcOffsetBackingStore", e);
        }
    }

    /**
     * 创建offset存储表（如果不存在）
     *
     * @throws SQLException SQL执行异常
     */
    private void createOffsetTableIfNotExists() throws SQLException {
        String createTableSQL = String.format(
                "CREATE TABLE IF NOT EXISTS %s (" +
                        "id BIGINT(20)      NOT NULL primary key ," +
                        "offset_key          VARCHAR(1255)," +
                        "offset_val          VARCHAR(1255)," +
                        "record_insert_ts    TIMESTAMP NOT NULL" +
                        ")", tableName);

        try (PreparedStatement stmt = connection.prepareStatement(createTableSQL)) {
            stmt.execute();
        }
    }

    /**
     * 从数据库获取指定key的offset值
     *
     * @param key 键
     * @return 对应的offset值
     */
    private ByteBuffer getOffset(ByteBuffer key) {
        String keyStr = bytesToString(key);
        String selectSQL = String.format("SELECT offset_val FROM %s WHERE offset_key = ? ORDER BY record_insert_ts desc limit 1", tableName);

        try (PreparedStatement stmt = connection.prepareStatement(selectSQL)) {
            stmt.setString(1, keyStr);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String valueStr = rs.getString(1);
                return StringUtils.isNotBlank(valueStr) ? ByteBuffer.wrap(valueStr.getBytes()) : null;
            }
        } catch (SQLException e) {
            log.error("Error getting offset for key: {}", keyStr, e);
        }
        return null;
    }

    /**
     * 将offset值存储到数据库
     * 这里插入和删除没有添加事务，是因为我这边想用一张表来存储多个task的 offset，测试当两个task同时删除数据时，会导致后者删除的操作失败
     * 在取数据时是按时间倒排，取最新一条，所以某次删除失败不影响最终结果
     *
     * @param key   键
     * @param value 值
     */
    private void setOffset(ByteBuffer key, ByteBuffer value) {
        if (Objects.isNull(key) || Objects.isNull(value)) {
            return;
        }
        String keyStr = bytesToString(key);
        byte[] valueBytes = value.array();
        String valueStr = new String(valueBytes, StandardCharsets.UTF_8);

        try {
            // 插入新的offset
            insertNewOffset(keyStr, valueStr);

            // 删除最旧的offset
            deleteOldestOffsetIfNeeded(keyStr);
            log.info("Offset stored success for key: {}, value: {}", keyStr, valueStr);
        } catch (SQLException e) {
            log.error("Error setting offset for key: {}", keyStr, e);
        }
    }

    private void insertNewOffset(String keyStr, String valueStr) throws SQLException {
        String upsertSQL = String.format(
                "INSERT INTO %s(id, offset_key, offset_val, record_insert_ts) VALUES ( ?, ?, ?, ? )",
                tableName);
        try (PreparedStatement stmt = connection.prepareStatement(upsertSQL)) {
            long id = SNOWFLAKE.nextId();
            stmt.setLong(1, id);
            stmt.setString(2, keyStr);
            stmt.setString(3, valueStr);
            stmt.setTimestamp(4, Timestamp.from(Instant.now()));
            stmt.executeUpdate();
        }
    }

    private void deleteOldestOffsetIfNeeded(String keyStr) {
        //count > 2 执行删除最旧的offset
        int count = 0;
        try (PreparedStatement stmt = connection.prepareStatement("SELECT COUNT(*) FROM " + tableName + " WHERE offset_key = ?")) {
            stmt.setString(1, keyStr);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            log.error("Error counting offsets", e);
        }
        if (count > 1) {
            String deleteSQL = String.format("DELETE FROM %s WHERE offset_key = ? " +
                            "AND id < (SELECT * FROM (SELECT MAX(id) FROM %s WHERE offset_key = ?) AS tmp)",
                    tableName, tableName);
            try (PreparedStatement stmt = connection.prepareStatement(deleteSQL)) {
                stmt.setString(1, keyStr);
                stmt.setString(2, keyStr);
                int deletedRows = stmt.executeUpdate();
                if (deletedRows > 0) {
                    log.info("Deleted oldest offset");
                }
            } catch (SQLException e) {
                log.error("Error deleting oldest offset", e);
            }
        }
    }

    /**
     * 将ByteBuffer转换为字符串表示
     *
     * @param buffer ByteBuffer对象
     * @return 字符串表示
     */
    private String bytesToString(ByteBuffer buffer) {
        if (Objects.isNull(buffer)) {
            return null;
        }
        byte[] bytes = new byte[buffer.remaining()];
        buffer.duplicate().get(bytes);
        return new String(bytes);
    }
}
