package org.dows.router.dao.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import org.dows.router.dao.entity.RouterMeteringEntity;
import org.dows.router.dao.mapper.RouterMeteringMapper;
import org.dows.router.dao.service.RouterMeteringService;
import org.springframework.stereotype.Service;

/**
 * 路由统计Service实现
 *
 * @author lait.zhang@gmail.com
 * @since 1.0.0
 */
@Service
public class RouterMeteringServiceImpl extends ServiceImpl<RouterMeteringMapper, RouterMeteringEntity> implements RouterMeteringService {

}