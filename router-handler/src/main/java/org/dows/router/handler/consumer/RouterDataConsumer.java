package org.dows.router.handler.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dows.router.entity.RouterDataEntity;
import org.dows.router.mapper.RouterDataMapper;
import org.dows.router.handler.model.RouterRequestTask;
import org.dows.router.handler.model.TaskStatus;
import org.dows.router.handler.producer.RouterDataProducer;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 路由数据消费者
 * 负责从数据库消费数据并做进一步处理
 *
 * @author lait.zhang@gmail.com
 * @since 1.0.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RouterDataConsumer {

    private final RouterDataMapper routerDataMapper;

    private final RouterDataProducer routerDataProducer;

    /**
     * 从 binlog 消费路由数据（新增方法）
     *
     * @param routerDataEntity 路由数据实体
     */
    public void consumeFromBinlog(RouterDataEntity routerDataEntity) {
        try {
            log.debug("开始从 binlog 消费路由数据: routerDataId={}", routerDataEntity.getRouterDataId());

            // 1. 数据验证
            if (!validateRouterData(routerDataEntity)) {
                throw new RuntimeException("路由数据验证失败");
            }

            // 2. 业务逻辑处理
            performBusinessLogicFromBinlog(routerDataEntity);

            // 3. 数据转换和清洗
            cleanAndTransformData(routerDataEntity);

            // 4. 触发后续流程
            triggerDownstreamProcessFromBinlog(routerDataEntity);

            // 5. 更新数据状态为已处理
            routerDataProducer.updateRouterDataStatus(routerDataEntity.getRouterDataId(), (byte) 2); // 2: 已处理

            log.info("从 binlog 消费路由数据完成: routerDataId={}", routerDataEntity.getRouterDataId());

        } catch (Exception e) {
            log.error("从 binlog 消费路由数据失败: routerDataId={}", routerDataEntity.getRouterDataId(), e);

            // 更新数据状态为处理失败
            routerDataProducer.updateRouterDataStatus(routerDataEntity.getRouterDataId(), (byte) 9); // 9: 处理失败

            throw new RuntimeException("从 binlog 消费路由数据失败", e);
        }
    }

    /**
     * 消费路由数据任务（保留原有方法用于兼容）
     *
     * @param task 路由请求任务
     */
    public void consume(RouterRequestTask task) {
        try {
            log.debug("开始消费路由数据: taskId={}", task.getTaskId());

            // 1. 获取数据库中的路由数据
            RouterDataEntity routerDataEntity = task.getRouterDataEntity();
            if (routerDataEntity == null || routerDataEntity.getRouterDataId() == null) {
                log.warn("路由数据实体为空，跳过消费: taskId={}", task.getTaskId());
                return;
            }

            // 2. 根据业务类型进行不同的处理
            switch (task.getTaskType()) {
                case ROUTER_DATA:
                    processRouterData(task, routerDataEntity);
                    break;
                case SESSION_DATA:
                    processSessionData(task, routerDataEntity);
                    break;
                default:
                    log.warn("未知的任务类型: taskId={}, taskType={}", task.getTaskId(), task.getTaskType());
            }

            // 3. 更新数据状态为已处理
            routerDataProducer.updateRouterDataStatus(routerDataEntity.getRouterDataId(), (byte) 2); // 2: 已处理

            log.info("路由数据消费完成: taskId={}, routerDataId={}",
                    task.getTaskId(), routerDataEntity.getRouterDataId());

        } catch (Exception e) {
            task.setStatus(TaskStatus.FAILED);
            task.setErrorMessage(e.getMessage());
            log.error("消费路由数据失败: taskId={}", task.getTaskId(), e);

            // 更新数据状态为处理失败
            if (task.getRouterDataEntity() != null && task.getRouterDataEntity().getRouterDataId() != null) {
                routerDataProducer.updateRouterDataStatus(task.getRouterDataEntity().getRouterDataId(), (byte) 9); // 9: 处理失败
            }

            throw new RuntimeException("消费路由数据失败", e);
        }
    }

    /**
     * 处理路由数据
     */
    private void processRouterData(RouterRequestTask task, RouterDataEntity routerDataEntity) {
        log.debug("处理路由数据: routerDataId={}", routerDataEntity.getRouterDataId());

        // 1. 数据验证
        if (!validateRouterData(routerDataEntity)) {
            throw new RuntimeException("路由数据验证失败");
        }

        // 2. 业务逻辑处理
        performBusinessLogic(task, routerDataEntity);

        // 3. 数据转换和清洗
        cleanAndTransformData(routerDataEntity);

        // 4. 触发后续流程
        triggerDownstreamProcess(task, routerDataEntity);

        log.debug("路由数据处理完成: routerDataId={}", routerDataEntity.getRouterDataId());
    }

    /**
     * 处理会话数据
     */
    private void processSessionData(RouterRequestTask task, RouterDataEntity routerDataEntity) {
        log.debug("处理会话数据: routerDataId={}", routerDataEntity.getRouterDataId());

        // 会话相关的特殊处理逻辑
        if (task.getRouterSessionEntity() != null) {
            // 更新会话信息
            updateSessionInfo(task);
        }

        // 执行会话相关的业务逻辑
        performSessionBusinessLogic(task, routerDataEntity);

        log.debug("会话数据处理完成: routerDataId={}", routerDataEntity.getRouterDataId());
    }

    /**
     * 验证路由数据
     */
    private boolean validateRouterData(RouterDataEntity routerDataEntity) {
        // 基本字段验证
        if (routerDataEntity.getAppId() == null || routerDataEntity.getAppId().trim().isEmpty()) {
            log.error("应用ID不能为空: routerDataId={}", routerDataEntity.getRouterDataId());
            return false;
        }

        if (routerDataEntity.getData() == null || routerDataEntity.getData().trim().isEmpty()) {
            log.error("数据内容不能为空: routerDataId={}", routerDataEntity.getRouterDataId());
            return false;
        }

        if (routerDataEntity.getRouterChannelId() == null) {
            log.error("路由通道ID不能为空: routerDataId={}", routerDataEntity.getRouterDataId());
            return false;
        }

        return true;
    }

    /**
     * 从 binlog 执行业务逻辑
     */
    private void performBusinessLogicFromBinlog(RouterDataEntity routerDataEntity) {
        // 模拟业务处理耗时
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("业务处理被中断", e);
        }

        // 根据优先级进行不同处理
        if (routerDataEntity.getPriority() != null && routerDataEntity.getPriority() > 5) {
            log.debug("高优先级数据处理: routerDataId={}, priority={}",
                    routerDataEntity.getRouterDataId(), routerDataEntity.getPriority());
            // 高优先级处理逻辑
        } else {
            log.debug("普通优先级数据处理: routerDataId={}", routerDataEntity.getRouterDataId());
            // 普通处理逻辑
        }

        // 根据应用ID进行特殊处理
        if ("SPECIAL_APP".equals(routerDataEntity.getAppId())) {
            log.debug("特殊应用数据处理: routerDataId={}", routerDataEntity.getRouterDataId());
            // 特殊应用处理逻辑
        }
    }

    /**
     * 从 binlog 触发下游处理流程
     */
    private void triggerDownstreamProcessFromBinlog(RouterDataEntity routerDataEntity) {
        // 根据业务需要触发下游处理
        log.debug("触发下游处理: routerDataId={}", routerDataEntity.getRouterDataId());

        // 可以在这里发送消息到其他系统
        // 或者调用其他服务的API
        // 或者写入其他队列等
    }

    /**
     * 执行业务逻辑（保留原有方法）
     */
    private void performBusinessLogic(RouterRequestTask task, RouterDataEntity routerDataEntity) {
        // 模拟业务处理耗时
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("业务处理被中断", e);
        }

        // 根据优先级进行不同处理
        if (routerDataEntity.getPriority() != null && routerDataEntity.getPriority() > 5) {
            log.debug("高优先级数据处理: routerDataId={}, priority={}",
                    routerDataEntity.getRouterDataId(), routerDataEntity.getPriority());
            // 高优先级处理逻辑
        } else {
            log.debug("普通优先级数据处理: routerDataId={}", routerDataEntity.getRouterDataId());
            // 普通处理逻辑
        }

        // 根据应用ID进行特殊处理
        if ("SPECIAL_APP".equals(routerDataEntity.getAppId())) {
            log.debug("特殊应用数据处理: routerDataId={}", routerDataEntity.getRouterDataId());
            // 特殊应用处理逻辑
        }
    }

    /**
     * 数据清洗和转换
     */
    private void cleanAndTransformData(RouterDataEntity routerDataEntity) {
        // 数据清洗逻辑
        String data = routerDataEntity.getData();
        if (data != null) {
            // 去除多余空格
            data = data.trim();
            // 数据格式化
            // data = formatData(data);
            routerDataEntity.setData(data);
        }

        // 更新处理时间
        routerDataEntity.setUt(LocalDateTime.now());
    }

    /**
     * 触发下游处理流程
     */
    private void triggerDownstreamProcess(RouterRequestTask task, RouterDataEntity routerDataEntity) {
        // 根据业务需要触发下游处理
        log.debug("触发下游处理: routerDataId={}", routerDataEntity.getRouterDataId());

        // 可以在这里发送消息到其他系统
        // 或者调用其他服务的API
        // 或者写入其他队列等
    }

    /**
     * 更新会话信息
     */
    private void updateSessionInfo(RouterRequestTask task) {
        if (task.getRouterSessionEntity() != null) {
            // 设置输出时间
            task.getRouterSessionEntity().setOutputTime(LocalDateTime.now());

            // 这里可以根据实际处理结果更新token和费用信息
            // 例如：根据处理的数据量计算token数和费用

            log.debug("会话信息已更新: routerDataId={}", task.getRouterSessionEntity().getRouterDataId());
        }
    }

    /**
     * 执行会话相关业务逻辑
     */
    private void performSessionBusinessLogic(RouterRequestTask task, RouterDataEntity routerDataEntity) {
        // 会话相关的特殊业务逻辑
        log.debug("执行会话业务逻辑: routerDataId={}", routerDataEntity.getRouterDataId());

        // 可以在这里添加会话相关的处理逻辑
        // 例如：会话状态管理、会话数据统计等
    }
}