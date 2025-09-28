package org.dows.router.config.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.dows.router.common.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 字段组管理控制器
 *
 * @author lait.zhang@gmail.com
 * @since 1.0.0
 */
@RestController
@Tag(name = "字段组", description = "字段组管理")
public class AdminFieldGroupRest implements FieldGroupApi {

    // @Autowired
    // private FieldGroupBiz fieldGroupBiz;

    @Override
    @PostMapping("/v1/admin/field/group/add")
    @Operation(summary = "新增对象")
    public FieldGroupAddResponse add(@RequestBody FieldGroupAddRequest request) {
        // TODO: 注入FieldGroupBiz后实现
        FieldGroupAddResponse response = new FieldGroupAddResponse();
        response.setSuccess(true);
        response.setMessage("新增成功");
        return response;
    }

    @Override
    @DeleteMapping("/v1/admin/field/group/remove/{id}")
    @Operation(summary = "根据ID逻辑删除")
    public void remove(@PathVariable("id") Long id) {
        // TODO: 实现逻辑删除
    }

    @Override
    @DeleteMapping("/v1/admin/field/group/delete/{id}")
    @Operation(summary = "根据ID物理删除")
    public void delete(@PathVariable("id") Long id) {
        // TODO: 实现物理删除
    }

    @Override
    @PutMapping("/v1/admin/field/group/update")
    @Operation(summary = "如果主键存在，根据ID更新")
    public FieldGroupUpdateResponse update(@RequestBody FieldGroupUpdateRequest request) {
        // TODO: 实现更新逻辑
        FieldGroupUpdateResponse response = new FieldGroupUpdateResponse();
        response.setSuccess(true);
        response.setMessage("更新成功");
        return response;
    }

    @Override
    @GetMapping("/v1/admin/field/group/page")
    @Operation(summary = "根据条件分页查询，返回分页对象")
    public Page<FieldGroupPageResponse> page(@ModelAttribute FieldGroupPageRequest request) {
        // TODO: 实现分页查询
        return new Page<>();
    }

    @Override
    @GetMapping("/v1/admin/field/group/get/{id}")
    @Operation(summary = "根据ID查询，返回一个对象")
    public FieldGroupGetResponse get(@PathVariable("id") Long id) {
        // TODO: 实现详情查询
        return new FieldGroupGetResponse();
    }

    @Override
    @GetMapping("/v1/admin/field/group/list")
    @Operation(summary = "根据条件查询，返回对象列表")
    public List<FieldGroupListResponse> list(@ModelAttribute FieldGroupListRequest request) {
        // TODO: 实现列表查询
        return List.of();
    }
}