package org.dows.router.handler;

import org.dows.router.dao.RouterDataDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 路由数据处理器
 *
 * @author lait.zhang@gmail.com
 * @since 1.0.0
 */
@Component
public class RouterDataHandler {

    @Autowired
    private RouterDataDao routerDataDao;

    // TODO: 实现路由数据处理逻辑
}