package org.dows.router.dao;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import org.dows.router.entity.RouterAccountEntity;
import org.dows.router.entity.RouterMeteringEntity;
import org.dows.router.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * 路由统计DAO实现
 */
@Repository
public class RouterMeteringDao extends ServiceImpl<RouterMeteringMapper, RouterMeteringEntity> {
}
