package org.dows.router.dao.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import org.dows.router.dao.entity.RouterSessionEntity;
import org.dows.router.dao.mapper.RouterSessionMapper;
import org.dows.router.dao.service.RouterSessionService;
import org.springframework.stereotype.Service;

/**
 * 路由会话Service实现
 *
 * @author lait.zhang@gmail.com
 * @since 1.0.0
 */
@Service
public class RouterSessionServiceImpl extends ServiceImpl<RouterSessionMapper, RouterSessionEntity> implements RouterSessionService {

}