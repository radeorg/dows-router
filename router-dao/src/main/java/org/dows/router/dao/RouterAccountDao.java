package org.dows.router.dao;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import org.dows.router.entity.RouterAccountEntity;
import org.dows.router.mapper.RouterAccountMapper;
import org.springframework.stereotype.Component;

@Component
public class RouterAccountDao extends ServiceImpl<RouterAccountMapper, RouterAccountEntity> {
}
