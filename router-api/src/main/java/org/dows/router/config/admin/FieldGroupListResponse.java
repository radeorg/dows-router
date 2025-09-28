package org.dows.router.config.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 字段组列表查询响应
 *
 * @author lait.zhang@gmail.com
 * @since 1.0.0
 */
@Data
@Schema(description = "字段组列表查询响应")
public class FieldGroupListResponse {

    @Schema(description = "字段组ID")
    private Long id;

    @Schema(description = "字段组名称")
    private String name;

    @Schema(description = "描述")
    private String description;

    @Schema(description = "应用ID")
    private String appId;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}