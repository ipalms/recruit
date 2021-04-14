package com.geek.geekstudio.interceptor;

import com.geek.geekstudio.annotaion.PassToken;
import com.geek.geekstudio.annotaion.UserLoginToken;
import com.geek.geekstudio.exception.NoTokenException;
import com.geek.geekstudio.exception.PermissionDeniedException;
import com.geek.geekstudio.exception.RecruitException;
import com.geek.geekstudio.model.po.AdminPO;
import com.geek.geekstudio.model.po.UserPO;
import com.geek.geekstudio.util.TokenUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
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
            //problem 拿到别人的token可以进行操作
            String userId = (String)redisTemplate.opsForValue().get(token);
            if(userId==null) {
                //userId为空 说明token过期 或 token是伪造的
                try {
                    TokenUtil.parseJWT(token);
                } catch (ExpiredJwtException e) {
                    throw new ExpiredJwtException(e.getHeader(),e.getClaims(),"token过期了");
                } catch (Exception k){
                    //否则就是伪造的token（或者token被篡改了，比如改了过期时间让其永不过期）--抛出权限不够等等..问题
                    throw new PermissionDeniedException("请登录");
                }
            }
             /*//如果当前的token还剩10分钟过期
            if(redisTemplate.getExpire(token)<CLOSE_TIME){
                redisTemplate.opsForValue().set(token,user,2, TimeUnit.HOURS);
            }*/
            return true;
        }
        return true;
    }
}
