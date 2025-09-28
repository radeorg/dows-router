package org.dows.router.config.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 字段组新增响应
 *
 * @author lait.zhang@gmail.com
 * @since 1.0.0
 */
@Data
@Schema(description = "字段组新增响应")
public class FieldGroupAddResponse {

    @Schema(description = "字段组ID")
    private Long id;

    @Schema(description = "操作结果")
    private Boolean success;

    @Schema(description = "消息")
    private String message;
}