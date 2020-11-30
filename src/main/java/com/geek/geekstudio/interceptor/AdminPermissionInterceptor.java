package com.geek.geekstudio.interceptor;


import com.geek.geekstudio.annotaion.AdminPermission;
import com.geek.geekstudio.exception.PermissionDeniedException;
import com.geek.geekstudio.model.po.AdminPO;
import com.geek.geekstudio.model.po.UserPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * 验证管理员权限，管理员和超级管理员放行
 */

@Component
public class AdminPermissionInterceptor implements HandlerInterceptor {
    //注入一个redisTemplate操纵redis缓存
    @Autowired
    RedisTemplate<Object,Object> redisTemplate;  //k-v都是对象的

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response , Object handler) throws PermissionDeniedException {
        //如果不是映射到方法直接通过
        if (!(handler instanceof HandlerMethod)){
            return true;
        }
        Method method = ((HandlerMethod) handler).getMethod();
        //如果有AdminPermission注释则需要验证
        if(method.isAnnotationPresent(AdminPermission.class)){
            Object user=redisTemplate.opsForValue().get(request.getHeader("token"));
            if(user instanceof AdminPO ){
                return true;
            }else{
                throw new PermissionDeniedException("权限不足！需要管理员权限！");
            }
        }
        return true;
    }
}
