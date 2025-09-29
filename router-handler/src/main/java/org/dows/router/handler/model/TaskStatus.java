package org.dows.router.handler.model;

/**
 * 任务状态枚举
 */
public enum TaskStatus {
    CREATED,        // 已创建
    QUEUED,         // 已入队
    PROCESSING,     // 处理中
    DB_WRITTEN,     // 已写入数据库
    CONSUMED,       // 已消费
    COMPLETED,      // 已完成
    FAILED          // 失败
}