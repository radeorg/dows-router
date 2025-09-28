package org.dows.router.config.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;

/**
 * 字段组新增请求
 *
 * @author lait.zhang@gmail.com
 * @since 1.0.0
 */
@Data
@Schema(description = "字段组新增请求")
public class FieldGroupAddRequest {

    @Schema(description = "字段组名称")
    @NotBlank(message = "字段组名称不能为空")
    private String name;

    @Schema(description = "描述")
    private String description;

    @Schema(description = "应用ID")
    private String appId;
}