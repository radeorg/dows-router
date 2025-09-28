package org.dows.router.config.admin;

import org.dows.router.common.Page;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 字段组业务逻辑
 *
 * @author lait.zhang@gmail.com
 * @since 1.0.0
 */
@Service
public class FieldGroupBiz {

    public FieldGroupAddResponse add(FieldGroupAddRequest request) {
        // TODO: 实现新增逻辑
        FieldGroupAddResponse response = new FieldGroupAddResponse();
        response.setSuccess(true);
        response.setMessage("新增成功");
        return response;
    }

    public void remove(Long id) {
        // TODO: 实现逻辑删除逻辑
    }

    public void delete(Long id) {
        // TODO: 实现物理删除逻辑
    }

    public FieldGroupUpdateResponse update(FieldGroupUpdateRequest request) {
        // TODO: 实现更新逻辑
        FieldGroupUpdateResponse response = new FieldGroupUpdateResponse();
        response.setSuccess(true);
        response.setMessage("更新成功");
        return response;
    }

    public Page<FieldGroupPageResponse> page(FieldGroupPageRequest request) {
        // TODO: 实现分页查询逻辑
        return new Page<>();
    }

    public FieldGroupGetResponse get(Long id) {
        // TODO: 实现详情查询逻辑
        return new FieldGroupGetResponse();
    }

    public List<FieldGroupListResponse> list(FieldGroupListRequest request) {
        // TODO: 实现列表查询逻辑
        return List.of();
    }
}