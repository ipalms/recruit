package com.geek.geekstudio.interceptor;

import com.geek.geekstudio.annotaion.SuperAdminPermission;
import com.geek.geekstudio.exception.PermissionDeniedException;
import com.geek.geekstudio.util.TokenUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
   验证超级管理员权限
 */

@Component
public class SuperAdminPermissionInterceptor implements HandlerInterceptor {
    @Autowired
    RedisTemplate<Object,Object> redisTemplate;  //k-v都是对象的

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response , Object handler) throws PermissionDeniedException {
        //如果不是映射到方法直接通过
        if (!(handler instanceof HandlerMethod)){
            return true;
        }

        Method method = ((HandlerMethod) handler).getMethod();

        //如果有AdminPermission注释则需要验证
        if(method.isAnnotationPresent(SuperAdminPermission.class)){
            Claims message=null;
            try {
                message= TokenUtil.parseJWT(request.getHeader("token"));
            } catch (ExpiredJwtException e) {
                throw new ExpiredJwtException(e.getHeader(),e.getClaims(),"token过期了");
            }
            //获得token存储的type属性
            String type=message.get("type", String.class);
            if("super".equals(type)){
                return true;
            }else{
                throw new PermissionDeniedException("权限不足！需要超级管理员权限！");
            }
        }
        return true;
    }
}
