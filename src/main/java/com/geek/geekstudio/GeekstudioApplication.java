package com.geek.geekstudio;

import com.geek.geekstudio.websocket.NettyServer;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
//开启缓存
@EnableCaching
//开启事务  基于子类实现的代理(CGLIB)
@EnableTransactionManagement(proxyTargetClass = true)
//扫描mapper包
@MapperScan("com.geek.geekstudio.mapper")
//扫描注册过滤器
@ServletComponentScan("com.geek.geekstudio.filter")
//定时任务
@EnableScheduling
//开启异步执行
@EnableAsync
public class GeekstudioApplication {
    public static void main(String[] args) {
        SpringApplication.run(GeekstudioApplication.class, args);
        try {
            //netty和springboot程序运行于不同的端口，即两程序运行的上下文是不同的--不能直接依赖注入
            //注意启动netty服务器的时候，如果netty中有阻塞方法（如channel().closeFuture().sync()）
            //会阻塞主线程的进行，如果spring boot启动类有其他逻辑处于netty服务器启动后执行就会被阻塞住
            System.out.println("http://127.0.0.1:8080/work");
            new NettyServer().start();
        } catch (Exception e) {
            System.out.println("启动netty出错:" + e.getMessage());
        }
        System.out.println("阻塞了！！！!!!");
    }
}
