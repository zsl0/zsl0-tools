package com.zsl0.util.async;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.TreeSet;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author zsl0
 * create on 2022/10/17 17:54
 */
public class ScheduleUtil {

    static Logger log = LoggerFactory.getLogger(ScheduleUtil.class);

    /**
     * 创建线程池
     */
    private static final ThreadPoolExecutor THREAD_POOL = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors() < 2 ? 1 : Runtime.getRuntime().availableProcessors() / 2 + 1,
            Runtime.getRuntime().availableProcessors() < 2 ? 1 : Runtime.getRuntime().availableProcessors() - 1,
            6, TimeUnit.MINUTES,
            new ArrayBlockingQueue<>(100), Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());

    private static TreeSet<TaskInfo> queue = new TreeSet<>();

    /**
     * 初始化
     */
    public static void init() {
        // todo 轮询查询队列

    }

    public static class TaskInfo {

    }
}
