package org.dows.router.handler.queue;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dows.router.core.Lifecycle;
import org.dows.router.handler.consumer.RouterDataConsumer;
import org.dows.router.handler.model.RouterRequestTask;
import org.dows.router.handler.model.TaskStatus;
import org.dows.router.handler.model.TaskType;
import org.dows.router.handler.producer.RouterDataProducer;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 异步路由队列服务
 * 基于BlockingQueue实现的异步处理机制
 *
 * @author lait.zhang@gmail.com
 * @since 1.0.0
 */
@RequiredArgsConstructor
@Slf4j
@Service
public class RouterQueueService implements Lifecycle {

    // 队列配置
    private static final int QUEUE_CAPACITY = 10000;
    private static final int DB_WRITER_THREADS = 3;
    private static final int CONSUMER_THREADS = 2;
    private static final long OFFER_TIMEOUT_MS = 100;

    // 内存队列 - 接收用户请求
    private final BlockingQueue<RouterRequestTask> requestQueue = new ArrayBlockingQueue<>(QUEUE_CAPACITY);

    // 数据库待处理队列 - 模拟数据库中的待处理数据
    private final BlockingQueue<RouterRequestTask> dbProcessQueue = new LinkedBlockingQueue<>();

    // 线程池
    private ExecutorService producerExecutor;
    private ExecutorService consumerExecutor;

    // 统计信息
    private final AtomicLong totalReceived = new AtomicLong(0);
    private final AtomicLong totalProcessed = new AtomicLong(0);
    private final AtomicLong totalFailed = new AtomicLong(0);

    // 运行状态
    private volatile boolean running = true;

    private final RouterDataProducer routerDataProducer;

    private final RouterDataConsumer routerDataConsumer;

    @Override
    public void init() {
        log.info("初始化异步路由队列服务...");

        // 创建线程池
        producerExecutor = Executors.newFixedThreadPool(DB_WRITER_THREADS,
                r -> new Thread(r, "router-producer-" + Thread.currentThread().getId()));
        consumerExecutor = Executors.newFixedThreadPool(CONSUMER_THREADS,
                r -> new Thread(r, "router-consumer-" + Thread.currentThread().getId()));

        // 启动数据库写入线程(3个并发写入)
        for (int i = 0; i < DB_WRITER_THREADS; i++) {
            producerExecutor.submit(this::startProducer);
        }

        // 注意：数据消费现在通过 binlog 进行，这里的消费线程主要用于兼容性
        // 实际的数据消费通过 BinlogListener 和 BinlogConsumer 完成
        /*for (int i = 0; i < CONSUMER_THREADS; i++) {
            consumerExecutor.submit(this::consumerWorker);
        }*/
        log.info("异步路由队列服务初始化完成 - 队列容量: {}, 写入线程: {}, 消费线程: {}",
                QUEUE_CAPACITY, DB_WRITER_THREADS, CONSUMER_THREADS);
    }

    @Override
    public void destroy() {
        log.info("关闭异步路由队列服务...");
        running = false;

        // 关闭线程池
        shutdownExecutor(producerExecutor, "producer");
        shutdownExecutor(consumerExecutor, "consumer");

        log.info("异步路由队列服务已关闭 - 总接收: {}, 总处理: {}, 总失败: {}",
                totalReceived.get(), totalProcessed.get(), totalFailed.get());
    }

    /**
     * 提交路由请求任务到队列
     */
    public String submitRouterRequest(String rawData, String appId, Long operatorId, Integer sessionType) {
        return submitRouterRequest(rawData, appId, operatorId, sessionType, 0);
    }

    /**
     * 提交路由请求任务到队列（带优先级）
     */
    public String submitRouterRequest(String rawData, String appId, Long operatorId, Integer sessionType, Integer priority) {
        try {
            // 创建任务
            RouterRequestTask task = new RouterRequestTask();
            task.setTaskId(UUID.randomUUID().toString());
            task.setTaskType(TaskType.ROUTER_DATA);
            task.setRawData(rawData);
            task.setAppId(appId);
            task.setOperatorId(operatorId);
            //task.setRouterChannelId(routerChannelId);
            task.setSessionType(sessionType);
            task.setPriority(priority);
            task.setRequestId(generateRequestId());

            // 尝试将任务放入队列，使用offer避免阻塞
            boolean offered = requestQueue.offer(task, OFFER_TIMEOUT_MS, TimeUnit.MILLISECONDS);

            if (offered) {
                task.setStatus(TaskStatus.QUEUED);
                task.setQueueTime(LocalDateTime.now());
                totalReceived.incrementAndGet();

                log.debug("路由任务已加入队列: taskId={}, appId={}, priority={}",
                        task.getTaskId(), appId, priority);
                return task.getTaskId();
            } else {
                // 队列满了，执行降级策略
                totalFailed.incrementAndGet();
                log.warn("队列已满，任务提交失败: appId={}, queueSize={}", appId, requestQueue.size());
                return null;
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("提交路由任务时被中断: appId={}", appId, e);
            return null;
        } catch (Exception e) {
            totalFailed.incrementAndGet();
            log.error("提交路由任务异常: appId={}", appId, e);
            return null;
        }
    }

    /**
     * 数据库写入工作线程
     */
    private void startProducer() {
        log.info("数据库写入线程启动: {}", Thread.currentThread().getName());

        while (running) {
            try {
                // 从请求队列中取数据，如果队列为空会阻塞等待
                RouterRequestTask task = requestQueue.take();

                task.setStatus(TaskStatus.PROCESSING);

                // 处理任务并写入数据库
                routerDataProducer.processAndSave(task);

                // 写入成功后，将任务放入处理队列
                task.setStatus(TaskStatus.DB_WRITTEN);
                task.setDbWriteTime(LocalDateTime.now());
                // 这里该为binlog消费
                dbProcessQueue.offer(task);

                log.debug("任务已写入数据库并加入处理队列: taskId={}", task.getTaskId());

            } catch (InterruptedException e) {
                log.info("数据库写入线程被中断: {}", Thread.currentThread().getName());
                Thread.currentThread().interrupt();
                break;
            } catch (Exception e) {
                log.error("数据库写入线程处理异常", e);
                totalFailed.incrementAndGet();
            }
        }

        log.info("数据库写入线程结束: {}", Thread.currentThread().getName());
    }

    /**
     * 数据消费工作线程
     */
    private void startConsumer() {
        log.info("数据消费线程启动: {}", Thread.currentThread().getName());

        while (running) {
            try {
                // 从处理队列中取数据，如果队列为空会阻塞等待
                RouterRequestTask task = dbProcessQueue.take();

                task.setStatus(TaskStatus.CONSUMED);

                // 进一步处理数据
                routerDataConsumer.consume(task);

                task.setStatus(TaskStatus.COMPLETED);
                task.setProcessTime(LocalDateTime.now());
                totalProcessed.incrementAndGet();

                log.debug("任务处理完成: taskId={}", task.getTaskId());

            } catch (InterruptedException e) {
                log.info("数据消费线程被中断: {}", Thread.currentThread().getName());
                Thread.currentThread().interrupt();
                break;
            } catch (Exception e) {
                log.error("数据消费线程处理异常", e);
                totalFailed.incrementAndGet();
            }
        }

        log.info("数据消费线程结束: {}", Thread.currentThread().getName());
    }

    /**
     * 关闭线程池
     */
    private void shutdownExecutor(ExecutorService executor, String name) {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(30, TimeUnit.SECONDS)) {
                log.warn("{}线程池未能在30秒内正常关闭，强制关闭", name);
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            log.error("等待{}线程池关闭时被中断", name, e);
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    /**
     * 生成请求ID
     */
    private String generateRequestId() {
        return "REQ_" + System.currentTimeMillis() + "_" + UUID.randomUUID().toString().substring(0, 8);
    }

    /**
     * 获取队列状态信息
     */
    public QueueStatus getQueueStatus() {
        QueueStatus status = new QueueStatus();
        status.setRequestQueueSize(requestQueue.size());
        status.setDbProcessQueueSize(dbProcessQueue.size());
        status.setTotalReceived(totalReceived.get());
        status.setTotalProcessed(totalProcessed.get());
        status.setTotalFailed(totalFailed.get());
        status.setRunning(running);
        return status;
    }


}