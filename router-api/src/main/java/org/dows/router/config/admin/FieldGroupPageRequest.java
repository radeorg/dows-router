package org.dows.router.config.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 字段组分页查询请求
 *
 * @author lait.zhang@gmail.com
 * @since 1.0.0
 */
@Data
@Schema(description = "字段组分页查询请求")
public class FieldGroupPageRequest {

    @Schema(description = "页码", defaultValue = "1")
    private Integer pageNumber = 1;

    @Schema(description = "页大小", defaultValue = "10")
    private Integer pageSize = 10;

    @Schema(description = "字段组名称")
    private String name;

    @Schema(description = "应用ID")
    private String appId;
}