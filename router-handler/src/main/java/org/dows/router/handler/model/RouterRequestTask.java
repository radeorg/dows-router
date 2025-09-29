package org.dows.router.handler.model;

import lombok.Data;
import org.dows.router.dao.entity.RouterDataEntity;
import org.dows.router.dao.entity.RouterSessionEntity;

import java.time.LocalDateTime;

/**
 * 路由请求任务模型
 *
 * @author lait.zhang@gmail.com
 * @since 1.0.0
 */
@Data
public class RouterRequestTask {

    /**
     * 任务ID
     */
    private String taskId;

    /**
     * 任务类型
     */
    private TaskType taskType;

    /**
     * 任务状态
     */
    private TaskStatus status;

    /**
     * 优先级
     */
    private Integer priority;

    /**
     * 原始请求数据
     */
    private String rawData;

    /**
     * 应用ID
     */
    private String appId;

    /**
     * 操作者ID
     */
    private Long operatorId;

    /**
     * 路由账户ID
     */
    private Long routerAccountId;

    /**
     * 路由通道ID
     */
    private Long routerChannelId;

    /**
     * 会话类型
     */
    private Integer sessionType;

    /**
     * 请求ID
     */
    private String requestId;

    /**
     * 重试次数
     */
    private Integer retry;

    /**
     * 路由数据实体（处理后）
     */
    private RouterDataEntity routerDataEntity;

    /**
     * 路由会话实体（处理后）
     */
    private RouterSessionEntity routerSessionEntity;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 入队时间
     */
    private LocalDateTime queueTime;

    /**
     * 数据库写入时间
     */
    private LocalDateTime dbWriteTime;

    /**
     * 处理完成时间
     */
    private LocalDateTime processTime;

    /**
     * 错误信息
     */
    private String errorMessage;





    public RouterRequestTask() {
        this.createTime = LocalDateTime.now();
        this.status = TaskStatus.CREATED;
        this.retry = 0;
    }

    public RouterRequestTask(String taskId, TaskType taskType) {
        this();
        this.taskId = taskId;
        this.taskType = taskType;
    }
}