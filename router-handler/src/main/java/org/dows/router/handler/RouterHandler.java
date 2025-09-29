package org.dows.router.handler;

import lombok.extern.slf4j.Slf4j;
import org.dows.router.handler.model.RouterRequest;
import org.dows.router.handler.queue.QueueStatus;
import org.dows.router.handler.queue.RouterQueueService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 异步路由服务
 *
 * @author lait.zhang@gmail.com
 * @since 1.0.0
 */
@Slf4j
@Service
public class RouterHandler {


    private RouterQueueService queueService;

    /**
     * 提交路由请求
     */
    public String submitRequest(String rawData, String appId, Long operatorId,
                                Long routerChannelId, Integer sessionType) {
        return submitRequest(rawData, appId, operatorId, routerChannelId, sessionType, 0);
    }

    /**
     * 提交路由请求（带优先级）
     */
    public String submitRequest(String rawData, String appId, Long operatorId,
                                Long routerChannelId, Integer sessionType, Integer priority) {
        try {
            log.info("接收路由请求: appId={}, operatorId={}, channelId={}, sessionType={}, priority={}",
                    appId, operatorId, routerChannelId, sessionType, priority);

            String taskId = queueService.submitRouterRequest(rawData, appId, operatorId, sessionType, priority);

            if (taskId != null) {
                log.info("路由请求提交成功: taskId={}, appId={}", taskId, appId);
                return taskId;
            } else {
                log.warn("路由请求提交失败: appId={}, 可能是队列已满", appId);
                return null;
            }

        } catch (Exception e) {
            log.error("提交路由请求异常: appId={}", appId, e);
            return null;
        }
    }

    /**
     * 获取队列状态
     */
    public QueueStatus getQueueStatus() {
        QueueStatus status = queueService.getQueueStatus();

        QueueStatus result = new QueueStatus();
        result.setRequestQueueSize(status.getRequestQueueSize());
        result.setDbProcessQueueSize(status.getDbProcessQueueSize());
        result.setTotalReceived(status.getTotalReceived());
        result.setTotalProcessed(status.getTotalProcessed());
        result.setTotalFailed(status.getTotalFailed());
        result.setRunning(status.isRunning());

        return result;
    }

    /**
     * 批量提交路由请求
     */
    public int batchSubmitRequests(List<RouterRequest> requests) {
        int successCount = 0;

        for (RouterRequest request : requests) {
            String taskId = submitRequest(
                    request.getRawData(),
                    request.getAppId(),
                    request.getOperatorId(),
                    request.getRouterChannelId(),
                    request.getSessionType(),
                    request.getPriority()
            );

            if (taskId != null) {
                successCount++;
            }
        }

        log.info("批量提交完成: 总数={}, 成功={}", requests.size(), successCount);
        return successCount;
    }


}