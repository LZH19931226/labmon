package com.hc.web.config;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 异步任务线程池配置
 *
 * Created by xxf on 2019-01-11.
 */
@Configuration
@ConfigurationProperties(prefix = "spring.task.pool")
public class TaskThreadPoolConfig {

    //核心线程数
    private int corePoolSize = 10;

    //最大线程数
    private int maxPoolSize = 50;

    //线程池维护线程所允许的空闲时间
    private int keepAliveSeconds = 60;

    //队列长度
    private int queueCapacity = 10000;

    //线程名称前缀
    private String threadNamePrefix = "xxf-AsyncTask-";

    public String getThreadNamePrefix() {
        return threadNamePrefix;
    }

    public void setThreadNamePrefix(String threadNamePrefix) {
        this.threadNamePrefix = threadNamePrefix;
    }

    public int getCorePoolSize() {
        return corePoolSize;
    }

    public void setCorePoolSize(int corePoolSize) {
        this.corePoolSize = corePoolSize;
    }

    public int getMaxPoolSize() {
        return maxPoolSize;
    }

    public void setMaxPoolSize(int maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }

    public int getKeepAliveSeconds() {
        return keepAliveSeconds;
    }

    public void setKeepAliveSeconds(int keepAliveSeconds) {
        this.keepAliveSeconds = keepAliveSeconds;
    }

    public int getQueueCapacity() {
        return queueCapacity;
    }

    public void setQueueCapacity(int queueCapacity) {
        this.queueCapacity = queueCapacity;
    }

}


