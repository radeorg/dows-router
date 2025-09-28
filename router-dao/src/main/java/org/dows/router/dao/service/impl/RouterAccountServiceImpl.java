package org.dows.router.dao.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import org.dows.router.dao.entity.RouterAccountEntity;
import org.dows.router.dao.mapper.RouterAccountMapper;
import org.dows.router.dao.service.RouterAccountService;
import org.springframework.stereotype.Service;

/**
 * 路由账户Service实现
 *
 * @author lait.zhang@gmail.com
 * @since 1.0.0
 */
@Service
public class RouterAccountServiceImpl extends ServiceImpl<RouterAccountMapper, RouterAccountEntity> implements RouterAccountService {

}