package org.dows.router.dao.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import org.dows.router.dao.entity.RouterDataEntity;
import org.dows.router.dao.mapper.RouterDataMapper;
import org.dows.router.dao.service.RouterDataService;
import org.springframework.stereotype.Service;

/**
 * 路由数据Service实现
 *
 * @author lait.zhang@gmail.com
 * @since 1.0.0
 */
@Service
public class RouterDataServiceImpl extends ServiceImpl<RouterDataMapper, RouterDataEntity> implements RouterDataService {

}