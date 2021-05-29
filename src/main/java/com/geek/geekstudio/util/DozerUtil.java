package com.geek.geekstudio.util;

import lombok.NoArgsConstructor;
import org.dozer.DozerBeanMapper;
import org.springframework.stereotype.Component;

/**
 * 数据模型之间相互转换
 * 双锁检测单例
 */

@NoArgsConstructor
@SuppressWarnings("all")
@Component
public class DozerUtil {

    //声明成 volatile，确保其可见性
    private volatile static DozerBeanMapper mapper;

    /**
     * 单例，双重判空
     */
    public static DozerBeanMapper getDozerBeanMapper() {
        if (mapper == null) {
            synchronized (DozerBeanMapper.class) {
                if (mapper == null) {
                    mapper = new DozerBeanMapper();
                }
            }
        }
        return mapper;
    }
}
