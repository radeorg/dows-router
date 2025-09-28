package org.dows.router.dao.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import org.dows.router.dao.entity.RouterSettingEntity;
import org.dows.router.dao.mapper.RouterSettingMapper;
import org.dows.router.dao.service.RouterSettingService;
import org.springframework.stereotype.Service;

/**
 * 路由设置Service实现
 *
 * @author lait.zhang@gmail.com
 * @since 1.0.0
 */
@Service
public class RouterSettingServiceImpl extends ServiceImpl<RouterSettingMapper, RouterSettingEntity> implements RouterSettingService {

}