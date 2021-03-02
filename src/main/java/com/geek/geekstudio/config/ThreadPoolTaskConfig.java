package com.geek.geekstudio.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池配置
 */
@Configuration
public class ThreadPoolTaskConfig {
    //注：如果参数是通过@Value注入的话，参数不能static修饰
    @Value("${async.executor.thread.core_pool_size}")
    /** 核心线程数（默认线程数），线程池中一直保留的线程数 */
    private int corePoolSize;

    @Value("${async.executor.thread.max_pool_size}")
    /** 最大线程数 当缓冲队列已满时可继续新建线程的数目（核心+非核心） */
    private int maxPoolSize;

    @Value("${async.executor.thread.alive_time}")
    /** 允许线程空闲时间（单位：默认为秒），非核心在该时间段内没有任务会销毁 */
    private int keepAliveTime;

    @Value("${async.executor.thread.queue_capacity}")
    /** 缓冲队列大小 */
    private int queueCapacity;

    @Value("${async.executor.thread.name.prefix}")
    /** 线程池名前缀 */
    private String threadNamePrefix;

    @Bean("taskExecutor") // bean的名称，默认为首字母小写的方法名
    public ThreadPoolTaskExecutor taskExecutor(){
        ThreadPoolTaskExecutor executor = new VisibleThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setKeepAliveSeconds(keepAliveTime);
        executor.setThreadNamePrefix(threadNamePrefix);
        // 线程池对拒绝任务的处理策略
        // CallerRunsPolicy：由调用线程（提交任务的线程）处理该任务
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 初始化
        executor.initialize();
        return executor;
    }
}
