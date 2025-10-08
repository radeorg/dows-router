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
 * 路由选项实体
 *
 * @author lait.zhang@gmail.com
 * @since 1.0.0
 */
@Table("router_options")
@Data
@Schema(description = "路由选项")
public class RouterOptionsEntity  extends BaseEntity<RouterOptionsEntity> {

    @Id(keyType = KeyType.Generator, value = KeyGenerators.snowFlakeId)
    @Schema(description = "路由选项ID")
    private Long routerOptionsId;

    @Column(value = "router_channel_id")
    @Schema(description = "路由通道ID")
    private Long routerChannelId;

    @Column(value = "router_config_id")
    @Schema(description = "路由配置ID")
    private Long routerConfigId;

    @Column(value = "item")
    @Schema(description = "选项item")
    private String item;

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