package com.geek.geekstudio.interceptor;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.geek.geekstudio.annotaion.PassToken;
import com.geek.geekstudio.annotaion.UserLoginToken;
import com.geek.geekstudio.exception.NoTokenException;
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
import java.util.concurrent.TimeUnit;

/**
  * 权限认证拦截器（是否携带了token）
 */
@Component
public class AuthenticationInterceptor implements HandlerInterceptor {
    private static final long CLOSE_TIME= 600;
    //注入一个redisTemplate操纵redis缓存
    @Autowired
    RedisTemplate<Object,Object> redisTemplate;  //k-v都是对象的
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws NoTokenException, PermissionDeniedException  {
        //SpringMVC会将请求通过处理器映射器将请求交给匹配的Handler处理，这个handler参数就是描述的处理请求的Handler。
        //如果不是映射到方法直接通过
        if (!(handler instanceof HandlerMethod)){
            return true;
        }
        //获取方法
        Method method = ((HandlerMethod) handler).getMethod();
        //如果有passToken注释，直接通过
        if(method.isAnnotationPresent(PassToken.class)){
            return true;
        }
        //如果有userLoginToken注释则需要验证
        if(method.isAnnotationPresent(UserLoginToken.class)){
            //从请求头中获取token
            String token = request.getHeader("token");
            if (token==null || "".equals(token)) {
                throw new NoTokenException();
            }

//            try {
//                //通过userId获取user信息，间接获取password
//                userId = JWT.decode(token).getAudience().get(0);
//            }catch (JWTDecodeException e){
//                throw new JWTDecodeException("401");
//            }

            //待完善  token即将过期但是操作使得token时间重置
            Object user =  redisTemplate.opsForValue().get(token);
            if(user==null) {
                throw new PermissionDeniedException();
            }
             /*//如果当前的token还剩10分钟过期
            if(redisTemplate.getExpire(token)<CLOSE_TIME){
                redisTemplate.opsForValue().set(token,user,2, TimeUnit.HOURS);
            }*/
            //token解密
            try {
                if(user instanceof UserPO){
                    UserPO userPO=(UserPO) user;
                    DecodedJWT jwt = JWT.require(Algorithm.HMAC256(userPO.getPassword())).build().verify(token);
                }else {
                    AdminPO adminPO=(AdminPO) user;
                    DecodedJWT jwt = JWT.require(Algorithm.HMAC256(adminPO.getPassword())).build().verify(token);
                }
            }catch (TokenExpiredException e){  //令牌过期处理
                //删除token后对象信息就丢失了，应该等重新申请token的时候再删除token
              /*  //UserMapUtil.userMap.remove(token);
                //删除redis中的令牌
                //redisTemplate.delete(token);*/
                throw new TokenExpiredException("资源访问受限!请重新登录！");
            }catch (JWTVerificationException e){
                throw new JWTVerificationException("401");
            }
            return true;
        }
        return true;
    }
}
