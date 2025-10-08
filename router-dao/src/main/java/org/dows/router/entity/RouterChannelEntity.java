package org.dows.router.entity;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import com.mybatisflex.core.keygen.KeyGenerators;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.dows.rade.crud.BaseEntity;

import java.time.LocalDateTime;

/**
 * 路由通道实体
 *
 * @author lait.zhang@gmail.com
 * @since 1.0.0
 */
@Table("router_channel")
@Data
@Schema(description = "路由通道")
public class RouterChannelEntity  extends BaseEntity<RouterChannelEntity> {

    @Id(keyType = KeyType.Generator, value = KeyGenerators.snowFlakeId)
    @Schema(description = "路由通道ID")
    private Long routerChannelId;

    @Column(value = "state")
    @Schema(description = "状态")
    private Integer state;

    @Column(value = "channel_code")
    @Schema(description = "通道码")
    private String channelCode;

    @Column(value = "channel_type")
    @Schema(description = "通道类型")
    private String channelType;

    @Column(value = "description")
    @Schema(description = "描述")
    private String description;

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