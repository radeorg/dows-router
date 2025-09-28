package org.dows.router.config.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotNull;

/**
 * 字段组更新请求
 *
 * @author lait.zhang@gmail.com
 * @since 1.0.0
 */
@Data
@Schema(description = "字段组更新请求")
public class FieldGroupUpdateRequest {

    @Schema(description = "字段组ID")
    @NotNull(message = "字段组ID不能为空")
    private Long id;

    @Schema(description = "字段组名称")
    private String name;

    @Schema(description = "描述")
    private String description;

    @Schema(description = "应用ID")
    private String appId;
}