package com.geek.geekstudio.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.geek.geekstudio.model.po.UserPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Date;
/**
 * Token工具类
 */
@Component
public class TokenUtil {

    @Autowired
    private RedisTemplate<Object,Object> redisTemplate;

    private static final String secret = "coffee";
    private static final String Subject = "recruit";

    public String create(UserPO userPO, Integer expireSeconds) {
        long time = System.currentTimeMillis();
        return JWT.create()
                /*设置 载荷 Payload*/
//                .withIssuer(Issuer)//签名是有谁生成 例如 服务器
//                .withSubject(Subject)//签名的主题
                .withAudience(userPO.getUserId() + "")//签名的观众 也可以理解谁接受签名的
                .withIssuedAt(new Date(time)) //生成签名的时间
                .withExpiresAt(new Date(time + expireSeconds * 1000))//签名过期的时间
                /*签名 Signature */
                .sign(Algorithm.HMAC256(userPO.getPassword()));
    }

    public DecodedJWT verify(String token) {
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret))
                    .build();
            return verifier.verify(token);
        } catch (JWTVerificationException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public DecodedJWT verify1(String token) {
        try {
            DecodedJWT verify = JWT.require(Algorithm.HMAC256(secret)).build().verify(token);
            return verify;
        } catch (JWTVerificationException e) {
            throw new TokenExpiredException("资源访问受限!请重新登录！");
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return null;
    }
}
