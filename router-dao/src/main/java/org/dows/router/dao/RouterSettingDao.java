package org.dows.router.dao;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import org.dows.router.entity.RouterAccountEntity;
import org.dows.router.entity.RouterSettingEntity;
import org.dows.router.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * 路由设置DAO实现
 */
@Repository
public class RouterSettingDao extends ServiceImpl<RouterSettingMapper, RouterSettingEntity> {
}
