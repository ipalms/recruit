package com.geek.geekstudio.config;

import com.geek.geekstudio.interceptor.AdminPermissionInterceptor;
import com.geek.geekstudio.interceptor.AuthenticationInterceptor;
import com.geek.geekstudio.interceptor.SuperAdminPermissionInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * SpringMVC的配置文件
 */
@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {
    private final AuthenticationInterceptor authenticationInterceptor;
    private final AdminPermissionInterceptor adminPermissionInterceptor;
    private final SuperAdminPermissionInterceptor superAdminPermissionInterceptor;

    //构造器注参
    @Autowired
    public WebMvcConfig(AuthenticationInterceptor authenticationInterceptor,
                        AdminPermissionInterceptor adminPermissionInterceptor,
                        SuperAdminPermissionInterceptor superAdminPermissionInterceptor) {
        this.authenticationInterceptor = authenticationInterceptor;
        this.adminPermissionInterceptor = adminPermissionInterceptor;
        this.superAdminPermissionInterceptor = superAdminPermissionInterceptor;
    }

    //加拦截器
    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        //1.加入的顺序就是拦截器执行的顺序，也可以使用order（）方法
        //2.按顺序执行所有拦截器的preHandle
        //3.所有的preHandle 执行完再执行全部postHandle 最后是afterCompletion
        registry.addInterceptor(authenticationInterceptor)
                //添加拦截规则
                .addPathPatterns("/**").order(1);
        //.excludePathPatterns("/");//排除拦截，表示该路径不用拦截（如首页）
        registry.addInterceptor(adminPermissionInterceptor).order(2);
        registry.addInterceptor(superAdminPermissionInterceptor).order(3);
        super.addInterceptors(registry);
    }

    //资源映射
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //和页面有关的静态目录都放在项目的static目录下,这里是做资源文件的放行
        //可以直接项目基地址加前端文件名访问到static目录下的资源文件（这里的配置向当于添加拦截器的排除拦截配置）
        registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
        //上传文件都在D盘下的all目录下，访问路径如：http://localhost:8080/source/image/picture5-778899.jpg
        //其中source表示访问的前缀。"file:D:/all/"是 windows系统下文件真实的存储路径
        //registry.addResourceHandler("/source/**").addResourceLocations("file:D:/all/");
        registry.addResourceHandler("/source/**").addResourceLocations("file:/all/");
    }

/*    //注册视图转换器
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/show").setViewName("file");
        registry.addViewController("/").setViewName("index");
        registry.addViewController("/index.html").setViewName("index");
    }*/
}
