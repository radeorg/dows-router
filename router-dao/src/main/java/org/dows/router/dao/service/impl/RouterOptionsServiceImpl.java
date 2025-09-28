package org.dows.router.dao.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import org.dows.router.dao.entity.RouterOptionsEntity;
import org.dows.router.dao.mapper.RouterOptionsMapper;
import org.dows.router.dao.service.RouterOptionsService;
import org.springframework.stereotype.Service;

/**
 * 路由选项Service实现
 *
 * @author lait.zhang@gmail.com
 * @since 1.0.0
 */
@Service
public class RouterOptionsServiceImpl extends ServiceImpl<RouterOptionsMapper, RouterOptionsEntity> implements RouterOptionsService {

}