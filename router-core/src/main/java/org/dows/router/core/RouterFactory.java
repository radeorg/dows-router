package org.dows.router.core;

import cn.hutool.extra.spring.SpringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class RouterFactory {

    //private final List<Routable> routables;

    public static Routable getRouter(int sessionType) {

        SessionTypeEnum sessionTypeEnum = SessionTypeEnum.getByType(sessionType);

        Map<String, Routable> beansOfType = SpringUtil.getBeansOfType(Routable.class);

        Routable routable = beansOfType.get(sessionTypeEnum.getBeanCode());

        if (routable != null) {
            return routable;
        }
        throw new RuntimeException("未找到对应的路由组件");
    }


}
