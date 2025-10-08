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
 * 路由设置实体
 *
 * @author lait.zhang@gmail.com
 * @since 1.0.0
 */
@Table("router_setting")
@Data
@Schema(description = "路由设置")
public class RouterSettingEntity  extends BaseEntity<RouterSettingEntity> {

    @Id(keyType = KeyType.Generator, value = KeyGenerators.snowFlakeId)
    @Schema(description = "路由设置ID")
    private Long routerSettingId;

    @Column(value = "router_config_id")
    @Schema(description = "路由配置ID")
    private Long routerConfigId;

    @Column(value = "identifier_id")
    @Schema(description = "主体标识ID[账号ID,组织ID...]")
    private Long identifierId;

    @Column(value = "identifier_type")
    @Schema(description = "主体标识类型[0:账号,1:组织]")
    private Integer identifierType;

    @Column(value = "val")
    @Schema(description = "键名对应的值")
    private String val;

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