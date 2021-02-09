package com.geek.geekstudio.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.lang.reflect.Method;

/**
 * @description : 异常捕获配置类（仅针对无返回值的异步方法的异常处理）
 */
//@Configuration
@Slf4j
public class AsyncExceptionConfig implements AsyncConfigurer {
    /**
     * @description  :   设置异步方法线程参数
     */
    /*@Bean
    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //根据自己机器配置
        executor.setCorePoolSize(8);
        executor.setMaxPoolSize(16);
        executor.setQueueCapacity(64);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.setThreadNamePrefix("SpringAsyncThread-");

        return executor;
    }*/

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new SpringAsyncExceptionHandler();
    }

    class SpringAsyncExceptionHandler implements AsyncUncaughtExceptionHandler {
        @Override
        public void handleUncaughtException(Throwable throwable, Method method, Object... objects) {
            log.info("------我是Async无返回方法的异常处理方法---------");
            //通过反射获取各个初始化方法名字
            String methodName = method.getName();
            log.info(" 当前异常方法名 ==  {} " , methodName);
            //根据方法名字 记录异常  略。。。。
            log.info("------我是Async无返回方法的异常处理方法---------");
        }
    }
}

