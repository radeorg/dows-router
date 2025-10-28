package org.dows.router.dao;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import org.dows.router.entity.RouterAccountEntity;
import org.dows.router.entity.RouterConfigEntity;
import org.dows.router.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * 路由配置DAO实现
 */
@Repository
public class RouterConfigDao extends ServiceImpl<RouterConfigMapper, RouterConfigEntity> {

}
