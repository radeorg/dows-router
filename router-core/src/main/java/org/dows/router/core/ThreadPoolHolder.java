package org.dows.router.core;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

// 静态内部类管理动态配置线程池
public class ThreadPoolHolder {
    // 动态读取配置参数（可从系统属性、配置文件或配置中心获取）
    private static final int CORE_POOL_SIZE = getConfigInt("router.threadpool.coreSize",
            Runtime.getRuntime().availableProcessors() * 2);
    private static final int MAX_POOL_SIZE = getConfigInt("router.threadpool.maxSize",
            Runtime.getRuntime().availableProcessors() * 4);
    private static final int QUEUE_CAPACITY = getConfigInt("router.threadpool.queueCapacity", 1024);
    private static final long KEEP_ALIVE_SECONDS = getConfigLong("router.threadpool.keepAliveSeconds", 60);

    // 动态配置的线程池
    private final static ThreadPoolExecutor EXECUTOR_SERVICE = new ThreadPoolExecutor(
            CORE_POOL_SIZE,
            MAX_POOL_SIZE,
            KEEP_ALIVE_SECONDS,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(QUEUE_CAPACITY),
            new ThreadPoolExecutor.CallerRunsPolicy() // 队列满时的拒绝策略，可根据需求调整
    );

    public static ThreadPoolExecutor getRouterExecutorService() {
        return EXECUTOR_SERVICE;
    }

    // 读取整数配置（带默认值）
    private static int getConfigInt(String key, int defaultValue) {
        try {
            String value = System.getProperty(key); // 优先从系统属性读取
            if (value == null) {
                value = System.getenv(key.toUpperCase().replace('.', '_')); // 其次从环境变量读取
            }
            return value != null ? Integer.parseInt(value) : defaultValue;
        } catch (Exception e) {
            return defaultValue; // 配置无效时使用默认值
        }
    }

    // 读取长整数配置（带默认值）
    private static long getConfigLong(String key, long defaultValue) {
        try {
            String value = System.getProperty(key);
            if (value == null) {
                value = System.getenv(key.toUpperCase().replace('.', '_'));
            }
            return value != null ? Long.parseLong(value) : defaultValue;
        } catch (Exception e) {
            return defaultValue;
        }
    }
}