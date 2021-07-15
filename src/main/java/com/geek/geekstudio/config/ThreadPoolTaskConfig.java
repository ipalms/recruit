package com.geek.geekstudio.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池配置
 */
@Configuration
@Slf4j
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
    public ThreadPoolTaskExecutor taskExecutor() {
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

    static class VisibleThreadPoolTaskExecutor extends ThreadPoolTaskExecutor {
        /**
         * showThreadPoolInfo方法中将任务总数、已完成数、活跃线程数，队列大小都打印出来了。
         * 然后Override了父类的execute、submit等方法，在里面调用showThreadPoolInfo方法，
         * 这样每次有任务被提交到线程池的时候，都会将当前线程池的基本情况打印到日志中；
         */
        private void showThreadPoolInfo(String prefix) {
            ThreadPoolExecutor threadPoolExecutor = getThreadPoolExecutor();
            log.info("{}, {},taskCount [{}], completedTaskCount [{}], activeCount [{}], queueSize [{}]",
                    this.getThreadNamePrefix(),
                    prefix,
                    threadPoolExecutor.getTaskCount(),
                    threadPoolExecutor.getCompletedTaskCount(),
                    threadPoolExecutor.getActiveCount(),
                    threadPoolExecutor.getQueue().size());
        }

        @Override
        public void execute(Runnable task) {
            showThreadPoolInfo("1. do execute");
            super.execute(task);
        }

        @Override
        public void execute(Runnable task, long startTimeout) {
            showThreadPoolInfo("2. do execute");
            super.execute(task, startTimeout);
        }

        @Override
        public Future<?> submit(Runnable task) {
            showThreadPoolInfo("1. do submit");
            return super.submit(task);
        }

        @Override
        public <T> Future<T> submit(Callable<T> task) {
            showThreadPoolInfo("2. do submit");
            return super.submit(task);
        }

        @Override
        public ListenableFuture<?> submitListenable(Runnable task) {
            showThreadPoolInfo("1. do submitListenable");
            return super.submitListenable(task);
        }

        @Override
        public <T> ListenableFuture<T> submitListenable(Callable<T> task) {
            showThreadPoolInfo("2. do submitListenable");
            return super.submitListenable(task);
        }
    }
}
