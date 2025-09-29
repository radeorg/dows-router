package org.dows.router.handler.binlog;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Binlog 事件处理器
 * 负责 binlog 事件的统计、错误记录等辅助功能
 *
 * @author lait.zhang@gmail.com
 * @since 1.0.0
 */
@Slf4j
@Component
public class BinlogEventProcessor {

    // 处理统计
    private final AtomicLong totalProcessed = new AtomicLong(0);
    private final AtomicLong totalSuccess = new AtomicLong(0);
    private final AtomicLong totalFailure = new AtomicLong(0);
    private volatile long lastProcessTime = 0;

    // 错误记录（内存中保存最近的错误，生产环境建议持久化）
    private final ConcurrentHashMap<Long, BinlogEventError> errorRecords = new ConcurrentHashMap<>();
    private static final int MAX_ERROR_RECORDS = 1000;

    /**
     * 记录处理成功
     */
    public void recordProcessSuccess(BinlogEvent event) {
        totalProcessed.incrementAndGet();
        totalSuccess.incrementAndGet();
        lastProcessTime = System.currentTimeMillis();

        log.debug("binlog 事件处理成功: routerDataId={}", event.getRouterDataId());
    }

    /**
     * 记录处理错误
     */
    public void recordProcessError(BinlogEvent event, Exception e) {
        totalProcessed.incrementAndGet();
        totalFailure.incrementAndGet();
        lastProcessTime = System.currentTimeMillis();

        // 记录错误详情
        BinlogEventError error = new BinlogEventError();
        error.setRouterDataId(event.getRouterDataId());
        error.setEventType(event.getEventType());
        error.setErrorMessage(e.getMessage());
        error.setErrorTime(LocalDateTime.now());
        error.setRetryCount(event.getRetryCount());

        // 保存错误记录（限制数量避免内存溢出）
        if (errorRecords.size() < MAX_ERROR_RECORDS) {
            errorRecords.put(event.getRouterDataId(), error);
        }

        log.error("binlog 事件处理失败: routerDataId={}, error={}",
                event.getRouterDataId(), e.getMessage());
    }

    /**
     * 获取处理统计信息
     */
    public BinlogProcessStats getProcessStats() {
        BinlogProcessStats stats = new BinlogProcessStats();
        stats.setTotalProcessed(totalProcessed.get());
        stats.setTotalSuccess(totalSuccess.get());
        stats.setTotalFailure(totalFailure.get());
        stats.setLastProcessTime(lastProcessTime);
        return stats;
    }

    /**
     * 获取错误记录
     */
    public java.util.Map<Long, BinlogEventError> getErrorRecords() {
        return new ConcurrentHashMap<>(errorRecords);
    }

    /**
     * 清理错误记录
     */
    public void clearErrorRecords() {
        errorRecords.clear();
        log.info("已清理 binlog 错误记录");
    }

    /**
     * 获取失败率
     */
    public double getFailureRate() {
        long total = totalProcessed.get();
        if (total == 0) {
            return 0.0;
        }
        return (double) totalFailure.get() / total * 100;
    }

    /**
     * 重置统计信息
     */
    public void resetStats() {
        totalProcessed.set(0);
        totalSuccess.set(0);
        totalFailure.set(0);
        lastProcessTime = 0;
        log.info("已重置 binlog 处理统计信息");
    }

    /**
     * Binlog 事件错误记录
     */
    public static class BinlogEventError {
        private Long routerDataId;
        private BinlogEvent.EventType eventType;
        private String errorMessage;
        private LocalDateTime errorTime;
        private Integer retryCount;

        // getters and setters
        public Long getRouterDataId() {
            return routerDataId;
        }

        public void setRouterDataId(Long routerDataId) {
            this.routerDataId = routerDataId;
        }

        public BinlogEvent.EventType getEventType() {
            return eventType;
        }

        public void setEventType(BinlogEvent.EventType eventType) {
            this.eventType = eventType;
        }

        public String getErrorMessage() {
            return errorMessage;
        }

        public void setErrorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
        }

        public LocalDateTime getErrorTime() {
            return errorTime;
        }

        public void setErrorTime(LocalDateTime errorTime) {
            this.errorTime = errorTime;
        }

        public Integer getRetryCount() {
            return retryCount;
        }

        public void setRetryCount(Integer retryCount) {
            this.retryCount = retryCount;
        }
    }
}