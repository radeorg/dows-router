package org.dows.router.dao;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import org.dows.router.entity.RouterAccountEntity;
import org.dows.router.entity.RouterSessionEntity;
import org.dows.router.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * 路由会话DAO实现
 */
@Repository
public class RouterSessionDao extends ServiceImpl<RouterSessionMapper, RouterSessionEntity> {
}
