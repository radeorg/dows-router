package org.dows.router.handler.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dows.router.dao.entity.RouterDataEntity;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class RouterDataTableEventHandler implements TableEventHandler<RouterDataEntity> {

    private final RouterDataCallback routerDataCallback;

    @Override
    public String getTableName() {
        return "router_data";
    }

    /**
     * 对RouterDataTable表的INSERT事件进行处理
     *
     * @param op
     * @param before
     * @param after
     */
    @Override
    public void insertHandle(String op, RouterDataEntity before, RouterDataEntity after) {
        // 仅处理 dev_bole.router_data 的 INSERT
        if (!Objects.equals(op, "c")) {
            return;
        }
        if (after == null) {
            log.warn("INSERT 事件缺少 after 数据，忽略");
            return;
        }
        Integer sessionType = after.getSessionType();
        // 提前定义好callback对象，用于异步线程种处理完成后的回调
        dispatch(sessionType, op, before, after, routerDataCallback);
        /*CompletableFuture<DispatchResult> future = CompletableFuture.supplyAsync(() -> {
            return dispatch(sessionType, op, before, after, routerDataCallback);
        });
        future.whenComplete((result, ex) -> {
            if (ex != null) {
                log.error("异步分发任务失败", ex);
            } else {
                log.info("异步分发任务完成，结果: {}", result);
                if (result != null && result.isSuccess()) {
                    // 处理成功逻辑
                } else {
                    // 处理失败逻辑
                }
            }
        });*/
        // 策略

        // 处理生命周期与记录统计


        // 记录处理成功
    }

}
