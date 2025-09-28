package org.dows.router.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 分页对象
 *
 * @author lait.zhang@gmail.com
 * @since 1.0.0
 */
@Data
@Schema(description = "分页对象")
public class Page<T> {

    @Schema(description = "当前页码")
    private Integer pageNumber;

    @Schema(description = "每页大小")
    private Integer pageSize;

    @Schema(description = "总记录数")
    private Long totalRow;

    @Schema(description = "总页数")
    private Integer totalPage;

    @Schema(description = "数据列表")
    private List<T> records;

    public Page() {
    }

    public Page(Integer pageNumber, Integer pageSize) {
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
    }
}