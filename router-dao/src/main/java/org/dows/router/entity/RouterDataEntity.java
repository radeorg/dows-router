package org.dows.router.entity;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import com.mybatisflex.core.keygen.KeyGenerators;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dows.rade.crud.BaseEntity;

import java.time.LocalDateTime;

/**
 * 路由数据实体
 *
 * @author lait.zhang@gmail.com
 * @since 1.0.0
 */
@Builder
@Table("router_data")
@EqualsAndHashCode(callSuper = true)
@Data(staticConstructor = "create")
@Schema(description = "路由数据")
public class RouterDataEntity extends BaseEntity<RouterDataEntity> {


    @Id(keyType = KeyType.Generator, value = KeyGenerators.flexId)
    @Schema(description = "路由数据ID")
    private Long routerDataId;

    @Column(value = "operator_id")
    @Schema(description = "操作者ID")
    private Long operatorId;

    @Column(value = "router_account_id")
    @Schema(description = "路由账户ID")
    private Long routerAccountId;

    @Column(value = "router_channel_id")
    @Schema(description = "路由通道ID")
    private Long routerChannelId;

    @Column(value = "retry")
    @Schema(description = "重试次数(默认0)")
    private Integer retry;

    @Column(value = "session_type")
    @Schema(description = "会话类型")
    private Integer sessionType;

    @Column(value = "priority")
    @Schema(description = "优先级")
    private Integer priority;

    @Column(value = "day")
    @Schema(description = "日")
    private Integer day;

    @Column(value = "month")
    @Schema(description = "月")
    private Integer month;

    @Column(value = "year")
    @Schema(description = "年")
    private Integer year;

    @Column(value = "request_id")
    @Schema(description = "请求ID")
    private String requestId;

    @Column(value = "data")
    @Schema(description = "数据")
    private String data;

    @Column(value = "app_id")
    @Schema(description = "应用id")
    private String appId;

    @Column(value = "state")
    @Schema(description = "状态")
    private Integer state;

    @Column(value = "deleted")
    @Schema(description = "是否删除:0未删除,1已删除")
    private Integer deleted;

    @Column(value = "ts")
    @Schema(description = "时间戳")
    private LocalDateTime ts;

    @Column(value = "ut")
    @Schema(description = "更新时间")
    private LocalDateTime ut;
}