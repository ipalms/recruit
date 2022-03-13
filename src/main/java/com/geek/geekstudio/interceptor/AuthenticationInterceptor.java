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
 * 过滤器与拦截器：
 * 过滤器:
 * 　　依赖于servlet容器。在实现上基于函数回调，可以对几乎所有请求进行过滤，但是缺点是一个过滤器实例只能在容器初始化时调用一次。
 *    使用过滤器的目的是用来做一些过滤操作，获取我们想要获取的数据.
 * 　　比如：在过滤器中修改字符编码；在过滤器中修改HttpServletRequest的一些参数，包括：过滤低俗文字、危险字符等
 *  拦截器:
 * 　　依赖于web框架，在SpringMVC中就是依赖于SpringMVC框架。在实现上基于Java的反射机制，属于面向切面编程（AOP）的一种运用。由于拦截器是基于Web框架的调用。
 * 　　因此可以使用spring的依赖注入（DI）进行一些业务操作，拦截器自身也要注入到容器当中，同时一个拦截器实例在一个controller生命周期之内可以多次调用
 *    可覆写的三个方法执行时机(拦截前（执行时机是在控制器方法执行之前）---拦截后（执行时机是在控制器方法执行之后，结果视图创建生成之前）--结果视图创建生成之后)。
 *    因为前后端交互不需要渲染视图所以第三个方法不需要使用
 * 总结：业务中尽量使用基于方法的拦截器，在进行一些需要统一处理的业务可以使用基于Servlet的过滤器
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
            //拿到别人的token可以进行操作--需要HTTPS才能保证通信双方的信息安全
            String userId = (String)redisTemplate.opsForValue().get(token);
            if(userId==null) {
                //userId为空 说明token过期 或 token是伪造的
                try {
                    //这里可以验证下是否这个token为刷新token，如果是刷新token是不允许进行登录操作的
                    //具体是要给刷新token设置一个独有的属性，没有这个属性的自然是普通token
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
