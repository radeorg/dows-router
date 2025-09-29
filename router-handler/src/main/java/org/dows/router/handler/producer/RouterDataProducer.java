package org.dows.router.handler.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dows.router.dao.entity.RouterDataEntity;
import org.dows.router.dao.entity.RouterSessionEntity;
import org.dows.router.dao.mapper.RouterDataMapper;
import org.dows.router.dao.mapper.RouterSessionMapper;
import org.dows.router.handler.model.RouterRequestTask;
import org.dows.router.handler.model.TaskStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 路由数据处理器
 * 负责将请求数据加工并写入数据库
 *
 * @author lait.zhang@gmail.com
 * @since 1.0.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RouterDataProducer {

    private final RouterDataMapper routerDataMapper;
    private final RouterSessionMapper routerSessionMapper;

    /**
     * 处理任务并保存到数据库，构建RouterDataEntity 和RouterSessionEntity
     *
     * @param task 路由请求任务
     */
    public void processAndSave(RouterRequestTask task) {
        try {
            log.debug("开始处理路由任务: taskId={}", task.getTaskId());

            // 1. 数据加工 - 准备 RouterDataEntity
            RouterDataEntity routerDataEntity = prepareRouterDataEntity(task);
            task.setRouterDataEntity(routerDataEntity);

            // 2. 写入 RouterData 表
            routerDataMapper.insert(routerDataEntity);
            log.debug("RouterData 已写入数据库: routerDataId={}", routerDataEntity.getRouterDataId());

            // 3. 如果需要，准备 RouterSessionEntity
            if (needCreateSession(task)) {
                RouterSessionEntity sessionEntity = prepareRouterSessionEntity(task, routerDataEntity);
                task.setRouterSessionEntity(sessionEntity);

                // 这里可以添加 RouterSessionMapper 来保存会话数据
                routerSessionMapper.insert(sessionEntity);
                log.debug("RouterSession 已准备: routerDataId={}", routerDataEntity.getRouterDataId());
            }

            log.info("路由任务处理完成: taskId={}, routerDataId={}",
                    task.getTaskId(), routerDataEntity.getRouterDataId());

        } catch (Exception e) {
            task.setStatus(TaskStatus.FAILED);
            task.setErrorMessage(e.getMessage());
            log.error("处理路由任务失败: taskId={}", task.getTaskId(), e);
            throw new RuntimeException("处理路由任务失败", e);
        }
    }

    /**
     * 准备 RouterDataEntity
     */
    private RouterDataEntity prepareRouterDataEntity(RouterRequestTask task) {
        RouterDataEntity entity = new RouterDataEntity();

        // 设置基本信息
        entity.setOperatorId(task.getOperatorId());
        entity.setRouterAccountId(task.getRouterAccountId());
        entity.setRouterChannelId(task.getRouterChannelId());
        entity.setRetry(task.getRetry());
        entity.setSessionType(task.getSessionType());
        entity.setPriority(task.getPriority());
        entity.setRequestId(task.getRequestId());
        entity.setData(task.getRawData());
        entity.setAppId(task.getAppId());

        // 设置时间信息
        LocalDateTime now = LocalDateTime.now();
        entity.setDay(now.getDayOfMonth());
        entity.setMonth(now.getMonthValue());
        entity.setYear(now.getYear());
        entity.setTs(now);
        entity.setUt(now);

        // 设置状态
        entity.setState((byte) 0); // 0: 待处理
        entity.setDeleted((byte) 0); // 0: 未删除

        return entity;
    }

    /**
     * 准备 RouterSessionEntity
     */
    private RouterSessionEntity prepareRouterSessionEntity(RouterRequestTask task, RouterDataEntity routerDataEntity) {
        RouterSessionEntity entity = new RouterSessionEntity();

        // 关联路由数据
        entity.setRouterDataId(routerDataEntity.getRouterDataId());
        entity.setRouterChannelId(task.getRouterChannelId());
        entity.setAppId(task.getAppId());

        // 设置时间信息
        LocalDateTime now = LocalDateTime.now();
        entity.setInputTime(now);
        entity.setTs(now);
        entity.setUt(now);

        // 初始化token和费用信息（后续可以根据实际处理结果更新）
        entity.setInputToken(0);
        entity.setInputFee(java.math.BigDecimal.ZERO);
        entity.setOutputToken(0);
        entity.setOutputFee(java.math.BigDecimal.ZERO);

        // 设置状态
        entity.setDeleted((byte) 0); // 0: 未删除

        return entity;
    }

    /**
     * 判断是否需要创建会话
     */
    private boolean needCreateSession(RouterRequestTask task) {
        // 根据业务逻辑判断是否需要创建会话
        // 例如：某些类型的请求需要创建会话记录
        return task.getSessionType() != null && task.getSessionType() > 0;
    }

    /**
     * 更新路由数据状态
     */
    public void updateRouterDataStatus(Long routerDataId, Byte newState) {
        try {
            RouterDataEntity entity = new RouterDataEntity();
            entity.setRouterDataId(routerDataId);
            entity.setState(newState);
            entity.setUt(LocalDateTime.now());

            //
            //routerDataMapper.updateByQuery(entity, query -> query.where(RouterDataEntity::getRouterDataId).eq(entity.getRouterDataId()));
            log.debug("更新路由数据状态: routerDataId={}, newState={}", routerDataId, newState);

        } catch (Exception e) {
            log.error("更新路由数据状态失败: routerDataId={}, newState={}", routerDataId, newState, e);
            throw new RuntimeException("更新路由数据状态失败", e);
        }
    }
}