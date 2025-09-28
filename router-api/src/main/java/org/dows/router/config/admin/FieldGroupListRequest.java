package org.dows.router.config.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 字段组列表查询请求
 *
 * @author lait.zhang@gmail.com
 * @since 1.0.0
 */
@Data
@Schema(description = "字段组列表查询请求")
public class FieldGroupListRequest {

    @Schema(description = "字段组名称")
    private String name;

    @Schema(description = "应用ID")
    private String appId;
}