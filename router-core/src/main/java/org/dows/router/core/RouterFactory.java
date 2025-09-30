package org.dows.router.core;

import cn.hutool.extra.spring.SpringUtil;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class RouterFactory {

    private static final Map<String, Routable> routerTable = new HashMap<>();

    public static Routable getRouter(int sessionType) {
        SessionTypeEnum sessionTypeEnum = SessionTypeEnum.getByType(sessionType);
        Routable routable = routerTable.get(sessionTypeEnum.getBeanCode());
        if (routable != null) {
            return routable;
        }
        throw new RuntimeException("未找到对应的路由组件");
    }

    @PostConstruct
    public void init() {
        routerTable.putAll(SpringUtil.getBeansOfType(Routable.class));
    }


}
