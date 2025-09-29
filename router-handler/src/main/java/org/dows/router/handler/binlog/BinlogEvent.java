package org.dows.router.handler.binlog;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * Binlog 事件模型
 * 表示数据库 binlog 中的变更事件
 *
 * @author lait.zhang@gmail.com
 * @since 1.0.0
 */
@Data
public class BinlogEvent {

    /**
     * 事件ID
     */
    private String eventId;

    /**
     * 数据库名
     */
    private String database;

    /**
     * 表名
     */
    private String tableName;

    /**
     * 事件类型
     */
    private EventType eventType;

    /**
     * binlog 位置信息
     */
    private String binlogFile;
    private Long binlogPosition;

    /**
     * 事件时间戳
     */
    private LocalDateTime eventTime;

    /**
     * RouterDataEntity 相关字段
     */
    private Long routerDataId;
    private Long operatorId;
    private Long routerAccountId;
    private Long routerChannelId;
    private Integer retry;
    private Integer sessionType;
    private Integer priority;
    private Integer day;
    private Integer month;
    private Integer year;
    private String requestId;
    private String data;
    private String appId;
    private Byte state;
    private Byte deleted;
    private LocalDateTime ts;
    private LocalDateTime ut;

    /**
     * 变更前的数据（用于 UPDATE 和 DELETE 事件）
     */
    private BinlogEvent beforeData;

    /**
     * 处理状态
     */
    private ProcessStatus processStatus;

    /**
     * 处理时间
     */
    private LocalDateTime processTime;

    /**
     * 错误信息
     */
    private String errorMessage;

    /**
     * 重试次数
     */
    private Integer retryCount;

    /**
     * 事件类型枚举
     */
    public enum EventType {
        INSERT,     // 插入
        UPDATE,     // 更新
        DELETE      // 删除
    }

    /**
     * 处理状态枚举
     */
    public enum ProcessStatus {
        PENDING,    // 待处理
        PROCESSING, // 处理中
        SUCCESS,    // 处理成功
        FAILED,     // 处理失败
        SKIPPED     // 跳过处理
    }

    public BinlogEvent() {
        this.eventTime = LocalDateTime.now();
        this.processStatus = ProcessStatus.PENDING;
        this.retryCount = 0;
    }

    /**
     * 创建 INSERT 事件
     */
    public static BinlogEvent createInsertEvent(String database, String tableName, 
                                               Long routerDataId, String appId) {
        BinlogEvent event = new BinlogEvent();
        event.setDatabase(database);
        event.setTableName(tableName);
        event.setEventType(EventType.INSERT);
        event.setRouterDataId(routerDataId);
        event.setAppId(appId);
        return event;
    }

    /**
     * 创建 UPDATE 事件
     */
    public static BinlogEvent createUpdateEvent(String database, String tableName,
                                               Long routerDataId, BinlogEvent beforeData) {
        BinlogEvent event = new BinlogEvent();
        event.setDatabase(database);
        event.setTableName(tableName);
        event.setEventType(EventType.UPDATE);
        event.setRouterDataId(routerDataId);
        event.setBeforeData(beforeData);
        return event;
    }

    /**
     * 创建 DELETE 事件
     */
    public static BinlogEvent createDeleteEvent(String database, String tableName,
                                               Long routerDataId) {
        BinlogEvent event = new BinlogEvent();
        event.setDatabase(database);
        event.setTableName(tableName);
        event.setEventType(EventType.DELETE);
        event.setRouterDataId(routerDataId);
        return event;
    }

    /**
     * 标记处理开始
     */
    public void markProcessing() {
        this.processStatus = ProcessStatus.PROCESSING;
        this.processTime = LocalDateTime.now();
    }

    /**
     * 标记处理成功
     */
    public void markSuccess() {
        this.processStatus = ProcessStatus.SUCCESS;
        this.processTime = LocalDateTime.now();
    }

    /**
     * 标记处理失败
     */
    public void markFailed(String errorMessage) {
        this.processStatus = ProcessStatus.FAILED;
        this.processTime = LocalDateTime.now();
        this.errorMessage = errorMessage;
        this.retryCount++;
    }

    /**
     * 标记跳过处理
     */
    public void markSkipped(String reason) {
        this.processStatus = ProcessStatus.SKIPPED;
        this.processTime = LocalDateTime.now();
        this.errorMessage = reason;
    }

    /**
     * 是否可以重试
     */
    public boolean canRetry(int maxRetryCount) {
        return this.retryCount < maxRetryCount && 
               ProcessStatus.FAILED.equals(this.processStatus);
    }

    /**
     * 重置为待处理状态（用于重试）
     */
    public void resetForRetry() {
        this.processStatus = ProcessStatus.PENDING;
        this.processTime = null;
        this.errorMessage = null;
    }
}