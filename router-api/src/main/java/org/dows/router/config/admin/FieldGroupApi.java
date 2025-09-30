package org.dows.router.config.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 字段组API接口
 *
 * @author lait.zhang@gmail.com
 * @since 1.0.0
 */
@Tag(name = "字段组", description = "字段组管理")
public interface FieldGroupApi {

    @PostMapping("/v1/admin/field/group/add")
    @Operation(summary = "新增对象")
    FieldGroupAddResponse add(@RequestBody FieldGroupAddRequest request);

    @DeleteMapping("/v1/admin/field/group/remove/{id}")
    @Operation(summary = "根据ID逻辑删除")
    void remove(@PathVariable("id") Long id);

    @DeleteMapping("/v1/admin/field/group/delete/{id}")
    @Operation(summary = "根据ID物理删除")
    void delete(@PathVariable("id") Long id);

    @PutMapping("/v1/admin/field/group/update")
    @Operation(summary = "如果主键存在，根据ID更新")
    FieldGroupUpdateResponse update(@RequestBody FieldGroupUpdateRequest request);

    @GetMapping("/v1/admin/field/group/page")
    @Operation(summary = "根据条件分页查询，返回分页对象")
    org.dows.router.common.Page<FieldGroupPageResponse> page(@ModelAttribute FieldGroupPageRequest request);

    @GetMapping("/v1/admin/field/group/get/{id}")
    @Operation(summary = "根据ID查询，返回一个对象")
    FieldGroupGetResponse get(@PathVariable("id") Long id);

    @GetMapping("/v1/admin/field/group/list")
    @Operation(summary = "根据条件查询，返回对象列表")
    List<FieldGroupListResponse> list(@ModelAttribute FieldGroupListRequest request);
}