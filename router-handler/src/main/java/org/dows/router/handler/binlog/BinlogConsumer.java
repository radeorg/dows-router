package org.dows.router.handler.binlog;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dows.router.dao.entity.RouterDataEntity;
import org.dows.router.handler.consumer.RouterDataConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Binlog 消费者
 * 监听 RouterDataEntity 表的 binlog 变化，消费新插入的数据
 *
 * @author lait.zhang@gmail.com
 * @since 1.0.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class BinlogConsumer {


    private final RouterDataConsumer routerDataConsumer;


    private final BinlogEventProcessor binlogEventProcessor;

    /**
     * 处理 binlog 事件
     * 当 RouterDataEntity 表有新数据插入时触发
     *
     * @param event binlog 事件
     */
    public void handleBinlogEvent(BinlogEvent event) {
        try {
            log.debug("接收到 binlog 事件: table={}, eventType={}, routerDataId={}",
                    event.getTableName(), event.getEventType(), event.getRouterDataId());

            // 只处理 RouterData 表的 INSERT 事件
            if (!"router_data".equals(event.getTableName()) ||
                    !BinlogEvent.EventType.INSERT.equals(event.getEventType())) {
                log.debug("忽略非 router_data INSERT 事件: table={}, eventType={}",
                        event.getTableName(), event.getEventType());
                return;
            }

            // 只处理状态为待处理的数据
            if (event.getState() == null || event.getState() != 0) {
                log.debug("忽略非待处理状态的数据: routerDataId={}, state={}",
                        event.getRouterDataId(), event.getState());
                return;
            }

            // 处理 binlog 事件
            processBinlogEvent(event);

        } catch (Exception e) {
            log.error("处理 binlog 事件失败: routerDataId={}", event.getRouterDataId(), e);
            // 可以在这里实现重试机制或者死信队列
            handleBinlogEventError(event, e);
        }
    }

    /**
     * 处理 binlog 事件的核心逻辑
     */
    private void processBinlogEvent(BinlogEvent event) {
        log.info("开始处理 binlog 事件: routerDataId={}", event.getRouterDataId());

        // 1. 根据 binlog 事件构建 RouterDataEntity
        RouterDataEntity routerDataEntity = buildRouterDataEntityFromBinlog(event);

        // 2. 调用数据消费者进行进一步处理
        routerDataConsumer.consumeFromBinlog(routerDataEntity);

        log.info("binlog 事件处理完成: routerDataId={}", event.getRouterDataId());
    }

    /**
     * 根据 binlog 事件构建 RouterDataEntity
     */
    private RouterDataEntity buildRouterDataEntityFromBinlog(BinlogEvent event) {
        RouterDataEntity entity = new RouterDataEntity();

        entity.setRouterDataId(event.getRouterDataId());
        entity.setOperatorId(event.getOperatorId());
        entity.setRouterAccountId(event.getRouterAccountId());
        entity.setRouterChannelId(event.getRouterChannelId());
        entity.setRetry(event.getRetry());
        entity.setSessionType(event.getSessionType());
        entity.setPriority(event.getPriority());
        entity.setDay(event.getDay());
        entity.setMonth(event.getMonth());
        entity.setYear(event.getYear());
        entity.setRequestId(event.getRequestId());
        entity.setData(event.getData());
        entity.setAppId(event.getAppId());
        entity.setState(event.getState());
        entity.setDeleted(event.getDeleted());
        entity.setTs(event.getTs());
        entity.setUt(event.getUt());

        return entity;
    }

    /**
     * 处理 binlog 事件错误
     */
    private void handleBinlogEventError(BinlogEvent event, Exception e) {
        log.error("binlog 事件处理失败，记录错误信息: routerDataId={}, error={}",
                event.getRouterDataId(), e.getMessage());

        // 可以实现以下错误处理策略：
        // 1. 重试机制
        // 2. 死信队列
        // 3. 告警通知
        // 4. 错误统计

        try {
            binlogEventProcessor.recordProcessError(event, e);
        } catch (Exception recordError) {
            log.error("记录 binlog 处理错误失败: routerDataId={}", event.getRouterDataId(), recordError);
        }
    }

    /**
     * 批量处理 binlog 事件
     * 用于处理积压的 binlog 事件
     */
    public void batchHandleBinlogEvents(java.util.List<BinlogEvent> events) {
        log.info("开始批量处理 binlog 事件: count={}", events.size());

        int successCount = 0;
        int failureCount = 0;

        for (BinlogEvent event : events) {
            try {
                handleBinlogEvent(event);
                successCount++;
            } catch (Exception e) {
                failureCount++;
                log.error("批量处理 binlog 事件失败: routerDataId={}", event.getRouterDataId(), e);
            }
        }

        log.info("批量处理 binlog 事件完成: total={}, success={}, failure={}",
                events.size(), successCount, failureCount);
    }

    /**
     * 获取处理统计信息
     */
    public BinlogProcessStats getProcessStats() {
        return binlogEventProcessor.getProcessStats();
    }


}