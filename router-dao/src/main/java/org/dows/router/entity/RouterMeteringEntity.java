package org.dows.router.entity;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import com.mybatisflex.core.keygen.KeyGenerators;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 路由统计实体
 *
 * @author lait.zhang@gmail.com
 * @since 1.0.0
 */
@Table("router_metering")
@Data
@Schema(description = "路由统计")
public class RouterMeteringEntity  {

    @Id(keyType = KeyType.Generator, value = KeyGenerators.snowFlakeId)
    @Schema(description = "路由计量ID")
    private Long routerMeteringId;

    @Column(value = "router_channel_id")
    @Schema(description = "路由通道ID")
    private Long routerChannelId;

    @Column(value = "account_instance_id")
    @Schema(description = "账号实例ID")
    private Long accountInstanceId;

    @Column(value = "counter")
    @Schema(description = "计数器(总次数)")
    private Long counter;

    @Column(value = "success_counter")
    @Schema(description = "成功次数")
    private Long successCounter;

    @Column(value = "fail_counter")
    @Schema(description = "失败次数")
    private Long failCounter;

    @Column(value = "success_rate")
    @Schema(description = "成功率")
    private BigDecimal successRate;

    @Column(value = "fail_rate")
    @Schema(description = "失败率")
    private BigDecimal failRate;

    @Column(value = "day_counter")
    @Schema(description = "日计数")
    private Integer dayCounter;

    @Column(value = "month_counter")
    @Schema(description = "月计数")
    private Integer monthCounter;

    @Column(value = "year_counter")
    @Schema(description = "年计数")
    private Integer yearCounter;

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