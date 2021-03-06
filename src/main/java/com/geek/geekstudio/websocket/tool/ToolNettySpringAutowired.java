package com.geek.geekstudio.websocket.tool;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Netty组件中获得ioc容器中的组件工具类
 * 实现bean对象管理（Autowired依赖注入）
 */
@Component
public class ToolNettySpringAutowired implements ApplicationContextAware {

    //注意  这里一定是要用static来修饰（表示他是属于类的），这样所有线程都是共享这个对象实例，才能实现容器注入
    //这也可以看作线程通信的方式，变相的也说明java中线程通信是靠共享内存来实现
    //netty线程可以 拿到spring ioc容器实例--依据ioc容器实例去拿到想要的组件（依赖注入）
    private static ApplicationContext applicationContext;

    //spring IOC线程启动时将拿到容器实例
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (ToolNettySpringAutowired.applicationContext == null) {
            ToolNettySpringAutowired.applicationContext = applicationContext;
        }
    }

    // 获取applicationContext
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    // 通过name获取 Bean
    public static Object getBean(String name) {
        return getApplicationContext().getBean(name);
    }

    // 通过class获取Bean
    public static <T> T getBean(Class<T> clazz) {
        return getApplicationContext().getBean(clazz);
    }

    // 通过name,以及Clazz返回指定的Bean
    public static <T> T getBean(String name, Class<T> clazz) {
        return getApplicationContext().getBean(name, clazz);
    }
}
