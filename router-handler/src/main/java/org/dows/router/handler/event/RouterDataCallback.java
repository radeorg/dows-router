package org.dows.router.handler.event;

import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dows.router.core.Callback;
import org.dows.router.core.RouterEvent;
import org.dows.router.core.RouterTriggerProperties;
import org.dows.router.core.Triggerable;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
@Slf4j
public class RouterDataCallback implements Callback {

    private final RouterTriggerProperties routerTriggerProperties;

    @Override
    public void onSuccess(RouterEvent routerEvent) {
        log.info("router data callback event：{}", JSONUtil.toJsonPrettyStr(routerEvent));
        // 获取事件远
        String source = routerEvent.getSource();
        // 获取事件名
        String eventName = routerEvent.getEventName();
        // 根据事件源和事件名查询触发表，找到target 进行触发
        List<RouterTriggerProperties.TriggerItem> triggerItems = routerTriggerProperties.getTrigger().get("${appid}");
        //

        RouterTriggerProperties.TriggerItem triggerItem = triggerItems.stream()
                .filter(item -> item.source().equals(source) && item.event().equals(eventName))
                .findAny()
                .orElse(null);

        if (triggerItem != null) {
            String target = triggerItem.target();
            log.info("触发器：{}", target);
            // todo trigger target
            String protocol = triggerItem.protocol();
            // todo  protocol:http,bean
            switch (protocol) {
                case "http":
                    // todo http
                    break;
                case "bean":
                    // todo bean
                    Triggerable bean = SpringUtil.getBean(target, Triggerable.class);
                    if (bean != null) {
                        /*
                         * // todo callback biz
                         * // LlmRequestData llmRequestData = RequestBuilder.build();
                         */
                        bean.trigger(routerEvent);
                    }
            }
        }
    }

    @Override
    public void onFailure(RouterEvent routerEvent) {
        // todo callback biz

    }
}
