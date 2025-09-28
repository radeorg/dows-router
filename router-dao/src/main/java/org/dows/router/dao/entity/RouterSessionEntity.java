package org.dows.router.dao.entity;

import com.mybatisflex.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 路由会话实体
 *
 * @author lait.zhang@gmail.com
 * @since 1.0.0
 */
@Table("router_session")
@Data
@Schema(description = "路由会话")
public class RouterSessionEntity {

    @Id(keyType = KeyType.Generator, value = KeyGenerators.snowFlakeId)
    @Schema(description = "路由会话ID")
    private Long routerSessionId;

    @Column(value = "router_data_id")
    @Schema(description = "路由数据ID")
    private Long routerDataId;

    @Column(value = "router_channel_id")
    @Schema(description = "路由通道ID")
    private Long routerChannelId;

    @Column(value = "input_time")
    @Schema(description = "输入时间")
    private LocalDateTime inputTime;

    @Column(value = "input_token")
    @Schema(description = "输入token数")
    private Integer inputToken;

    @Column(value = "input_fee")
    @Schema(description = "输入费用")
    private BigDecimal inputFee;

    @Column(value = "output_token")
    @Schema(description = "输出token数")
    private Integer outputToken;

    @Column(value = "output_time")
    @Schema(description = "输出时间")
    private LocalDateTime outputTime;

    @Column(value = "output_fee")
    @Schema(description = "输出费用")
    private BigDecimal outputFee;

    @Column(value = "app_id")
    @Schema(description = "应用id")
    private String appId;

    @Column(value = "deleted")
    @Schema(description = "是否删除:0未删除,1已删除")
    private Byte deleted;

    @Column(value = "ts")
    @Schema(description = "时间戳")
    private LocalDateTime ts;

    @Column(value = "ut")
    @Schema(description = "更新时间")
    private LocalDateTime ut;
}