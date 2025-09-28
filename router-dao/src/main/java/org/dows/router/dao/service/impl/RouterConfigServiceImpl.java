package org.dows.router.dao.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import org.dows.router.dao.entity.RouterConfigEntity;
import org.dows.router.dao.mapper.RouterConfigMapper;
import org.dows.router.dao.service.RouterConfigService;
import org.springframework.stereotype.Service;

/**
 * 路由配置Service实现
 *
 * @author lait.zhang@gmail.com
 * @since 1.0.0
 */
@Service
public class RouterConfigServiceImpl extends ServiceImpl<RouterConfigMapper, RouterConfigEntity> implements RouterConfigService {

}