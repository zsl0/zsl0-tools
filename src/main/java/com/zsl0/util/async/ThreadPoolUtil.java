package com.zsl0.util.async;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.concurrent.*;

/**
 * 简单实现定时任务
 *  使用startCpuSchedule或startIoSchedule函数，并传入轮询延迟时间启动轮询任务；
 *  返回任务对象;
 *  todo
 *      1.实现返回对象(包含对象，进行任务添加、删除) 可选单例
 *      2.queue更改为对象使用触发时间排序(TreeSet)
 *
 * @author zsl0
 * create on 2022/10/17 17:54
 */
public class ThreadPoolUtil {

    static Logger log = LoggerFactory.getLogger(ThreadPoolUtil.class);

    /** True if on multiprocessor */
    private static final boolean MP =
            Runtime.getRuntime().availableProcessors() > 1;

//
//
//    /**
//     * cpu轮询任务池
//     */
//    static ScheduledThreadPoolExecutor CPU_POLL_TASK_POOL = new ScheduledThreadPoolExecutor(1,
//            Executors.defaultThreadFactory(),
//            new ThreadPoolExecutor.AbortPolicy());
//
//    /**
//     * io轮询任务池
//     */
//    static ScheduledThreadPoolExecutor IO_POLL_TASK_POOL = new ScheduledThreadPoolExecutor(1,
//            Executors.defaultThreadFactory(),
//            new ThreadPoolExecutor.AbortPolicy());
//
//
//    /**
//     * cpu任务队列
//     */
//    static TreeMap<LocalDateTime, Runnable> CPU_QUEUE = new TreeMap<>();
//
//    /**
//     * io任务队列
//     */
//    static TreeMap<LocalDateTime, Runnable> IO_QUEUE = new TreeMap<>();
//
//
//    /**
//     * cpu密集线程池
//     */
//    static ThreadPoolExecutor CPU_THREAD_POOL = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors() > 2 ? Runtime.getRuntime().availableProcessors() / 2 + 1 : 1,
//            Runtime.getRuntime().availableProcessors() > 2 ? Runtime.getRuntime().availableProcessors() - 1 : 1,
//            60, TimeUnit.SECONDS,
//            new ArrayBlockingQueue<>(0),
//            Executors.defaultThreadFactory(),
//            new ThreadPoolExecutor.AbortPolicy());
//
//    /**
//     * i/o密集线程池
//     */
//    static ThreadPoolExecutor IO_THREAD_POOL = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors() > 2 ? Runtime.getRuntime().availableProcessors() / 2 + 1 : 1,
//            Runtime.getRuntime().availableProcessors() > 2 ? Runtime.getRuntime().availableProcessors() - 1 : 1,
//            60, TimeUnit.SECONDS,
//            new ArrayBlockingQueue<>(Runtime.getRuntime().availableProcessors() * 4),
//            Executors.defaultThreadFactory(),
//            new ThreadPoolExecutor.AbortPolicy());
//
//    /**
//     * 启动cpu轮询
//     * @param delay 延迟时间长度
//     * @param timeUnit 延迟时间单位
//     */
//    public static void startCpuSchedule(long delay, TimeUnit timeUnit) {
//        start(CPU_POLL_TASK_POOL, CPU_QUEUE, CPU_THREAD_POOL, delay, timeUnit);
//    }
//
//    /**
//     * 启动io轮询
//     * @param delay 延迟时间长度
//     * @param timeUnit 延迟时间单位
//     */
//    public static void startIoSchedule(long delay, TimeUnit timeUnit) {
//        start(IO_POLL_TASK_POOL, IO_QUEUE, IO_THREAD_POOL, delay, timeUnit);
//    }

    /**
     * 启动轮询，并执行
     * @param scheduled 轮询线程池
     * @param queue 任务队列
     * @param executor 执行任务线程池
     * @param delay 延迟时间长度
     * @param timeUnit 延迟时间单位
     */
    private static void start(ScheduledThreadPoolExecutor scheduled, TreeMap<LocalDateTime, Runnable> queue, ThreadPoolExecutor executor, long delay, TimeUnit timeUnit) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                // 轮询查看是否有任务需要执行
                while (Objects.nonNull(queue) && !queue.firstEntry().getKey().isAfter(LocalDateTime.now())) {
                    Map.Entry<LocalDateTime, Runnable> entry = queue.pollFirstEntry();
                    Runnable value = entry.getValue();

                    executor.execute(value);
                }

                scheduled.schedule(this, delay, timeUnit);
            }
        };

        // 启动轮询任务
        scheduled.schedule(runnable, delay, timeUnit);
    }


    /**
     * 创建cpu密集型线程池
     * @param keepAliveTimeSeconds (maxPoolSize - coreSize)部分线程的保持存活时间
     * @param BlockingQueueCapacity 阻塞队列容量
     * @param handler 拒绝策略
     * @return 线程池
     */
    public static ThreadPoolExecutor createCPUThreadPool(int keepAliveTimeSeconds, int BlockingQueueCapacity, RejectedExecutionHandler handler) {
        // 获取核心数、最大线程数
        int coreSize = MP ? Runtime.getRuntime().availableProcessors() - 1 : 1;

        return createDefaultThreadPool(coreSize,
                coreSize,
                keepAliveTimeSeconds,
                BlockingQueueCapacity,
                handler);
    }

    /**
     * 创建io密集型线程池
     * @param keepAliveTimeSeconds (maxPoolSize - coreSize)部分线程的保持存活时间
     * @param BlockingQueueCapacity 阻塞队列容量
     * @param handler 拒绝策略
     * @return 线程池
     */
    public static ThreadPoolExecutor createIOThreadPool(int keepAliveTimeSeconds, int BlockingQueueCapacity, RejectedExecutionHandler handler) {
        // 获取核心数、最大线程数
        int coreSize = MP ? Runtime.getRuntime().availableProcessors() - 1 : 1;
        int maxPoolSize = coreSize * 4;

        return createDefaultThreadPool(coreSize,
                maxPoolSize,
                keepAliveTimeSeconds,
                BlockingQueueCapacity,
                handler);
    }

    /**
     * 创建默认线程池
     * @param corePoolSize 线程池核心数
     * @param maximumPoolSize 线程池最大核心数
     * @param keepAliveTime 保持存活时间（秒）
     * @param BlockingQueueCapacity 阻塞队列大小
     * @param handler 拒绝策略
     * @return 线程池
     */
    private static ThreadPoolExecutor createDefaultThreadPool(int corePoolSize,
                                                              int maximumPoolSize,
                                                              long keepAliveTime,
                                                              int BlockingQueueCapacity,
                                                              RejectedExecutionHandler handler) {
        return new ThreadPoolExecutor(corePoolSize,
                maximumPoolSize,
                keepAliveTime,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(BlockingQueueCapacity),
                Executors.defaultThreadFactory(),
                handler);
    }
}
