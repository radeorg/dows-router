package org.dows.router.handler.queue;

import lombok.Data;

/**
 * 队列状态信息
 */
@Data
public class QueueStatus {
    private int requestQueueSize;
    private int dbProcessQueueSize;
    private long totalReceived;
    private long totalProcessed;
    private long totalFailed;
    private boolean running;
}