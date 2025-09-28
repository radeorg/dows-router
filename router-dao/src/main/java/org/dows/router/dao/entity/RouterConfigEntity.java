package org.dows.router.dao.entity;

import com.mybatisflex.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 路由配置实体
 *
 * @author lait.zhang@gmail.com
 * @since 1.0.0
 */
@Table("router_config")
@Data
@Schema(description = "路由配置")
public class RouterConfigEntity {

    @Id(keyType = KeyType.Generator, value = KeyGenerators.snowFlakeId)
    @Schema(description = "路由配置ID")
    private Long routerConfigId;

    @Column(value = "router_channel_id")
    @Schema(description = "路由通道ID")
    private Long routerChannelId;

    @Column(value = "key")
    @Schema(description = "配置键名")
    private String key;

    @Column(value = "label")
    @Schema(description = "标签")
    private String label;

    @Column(value = "tag")
    @Schema(description = "元素标签[input,select,checkbox...]")
    private String tag;

    @Column(value = "datatype")
    @Schema(description = "数据类型")
    private String datatype;

    @Column(value = "description")
    @Schema(description = "描述")
    private String description;

    @Column(value = "definition")
    @Schema(description = "自定义标识")
    private Boolean definition;

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