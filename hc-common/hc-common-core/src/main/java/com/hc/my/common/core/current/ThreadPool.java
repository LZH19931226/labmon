package com.hc.my.common.core.current;

import java.util.concurrent.*;

/**
 * @author LiuZhiHao
 * @date 2019/10/24 14:04
 * 描述:
 **/
public final class ThreadPool {

    /**
     * create thread pool with fixed queue size.
     *
     * @param fixed queue size
     * @return thread poll
     */
    public static ExecutorService createThreadPool(int fixed) {
        int processorsOfCPU = Runtime.getRuntime().availableProcessors();
        return new ThreadPoolExecutor(2 * processorsOfCPU,
                2 * processorsOfCPU,
                5L,
                TimeUnit.MINUTES,
                new LimitedQueue<>(fixed),
                new NamedThreadFactory("process-multi-execute-thread"));
    }

    public static ExecutorService create(int fixed) {
        return createThreadPool(fixed);
    }

    public static ScheduledExecutorService scheduled(int queue) {
        int processorsOfCPU = Runtime.getRuntime().availableProcessors();
        return new ScheduledThreadPoolExecutor(2 * processorsOfCPU,
                new NamedThreadFactory("process-multi-scheduled-thread"));
    }

}
