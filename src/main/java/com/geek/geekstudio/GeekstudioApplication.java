package com.geek.geekstudio;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
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
public class GeekstudioApplication {

    public static void main(String[] args) {
        SpringApplication.run(GeekstudioApplication.class, args);
    }
}
