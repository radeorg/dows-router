package org.dows.router.dao.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import org.dows.router.dao.entity.RouterChannelEntity;
import org.dows.router.dao.mapper.RouterChannelMapper;
import org.dows.router.dao.service.RouterChannelService;
import org.springframework.stereotype.Service;

/**
 * 路由通道Service实现
 *
 * @author lait.zhang@gmail.com
 * @since 1.0.0
 */
@Service
public class RouterChannelServiceImpl extends ServiceImpl<RouterChannelMapper, RouterChannelEntity> implements RouterChannelService {

}