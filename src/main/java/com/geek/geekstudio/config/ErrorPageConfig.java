package com.geek.geekstudio.config;

import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

@Configuration
public class ErrorPageConfig {
    //配置404页面路径统一跳转
    @Bean
    public WebServerFactoryCustomizer<ConfigurableWebServerFactory> webServerFactoryCustomizer() {
/*        //第一种：java7 常规写法
        return new WebServerFactoryCustomizer<ConfigurableWebServerFactory>() {
            @Override
            public void customize(ConfigurableWebServerFactory factory) {
                ErrorPage errorPage404 = new ErrorPage(HttpStatus.NOT_FOUND, "/index.html");
                factory.addErrorPages(errorPage404);
            }
        };*/
        //第二种写法：java8 lambda写法
        return ((factory) -> {
            ErrorPage errorPage404 = new ErrorPage(HttpStatus.MOVED_TEMPORARILY, "/index.html");
            factory.addErrorPages(errorPage404);
        });
    }
}
