package org.dows.router.core.config;

import lombok.extern.slf4j.Slf4j;
import org.dows.router.core.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

/**
 * 路由器核心配置
 *
 * @author lait.zhang@gmail.com
 * @since 1.0.0
 */
@Slf4j
@Configuration
public class RouterConfig {

    // TODO: 添加核心配置
    @Autowired
    private Lifecycle lifecycle;

    /**
     * Spring 容器启动完成后初始化异步队列服务
     */
    @EventListener(ContextRefreshedEvent.class)
    public void onApplicationReady() {
        try {
            log.info("Spring 容器启动完成，开始初始化异步路由队列服务...");
            lifecycle.init();
            log.info("异步路由队列服务初始化完成");
        } catch (Exception e) {
            log.error("初始化异步路由队列服务失败", e);
            throw new RuntimeException("异步路由队列服务启动失败", e);
        }
    }

    /**
     * 应用关闭时的清理工作
     */
    @EventListener/*(ContextClosedEvent.class)*/
    public void onApplicationShutdown(ContextClosedEvent event) {
        try {
            log.info("event :{}", event);
            log.info("应用关闭中，开始清理异步路由队列服务...");
            lifecycle.destroy();
            log.info("异步路由队列服务清理完成");
        } catch (Exception e) {
            log.error("清理异步路由队列服务失败", e);
        }
    }


}