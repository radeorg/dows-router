package org.dows.router.handler.event;

import cn.hutool.json.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.dows.router.dao.entity.RouterDataEntity;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@Component
public class RouterDataTableEventHandler implements TableEventHandler<RouterDataEntity> {


    @Override
    public String getTableName() {
        return "router_data";
    }

    @Override
    public void handle(String op, JSONObject before, JSONObject after) {
        // 仅处理 dev_bole.router_data 的 INSERT
        if (!Objects.equals(op, "c")) {
            return;
        }
        if (after == null) {
            log.warn("INSERT 事件缺少 after 数据，忽略");
            return;
        }
        RouterDataEntity beforeEntity = convert(before);
        RouterDataEntity afterEntity = convert(after);
        Integer sessionType = afterEntity.getSessionType();
        dispatch(sessionType,op,beforeEntity,afterEntity);
        // 策略

        // 处理生命周期与记录统计


        // 记录处理成功

    }
}
