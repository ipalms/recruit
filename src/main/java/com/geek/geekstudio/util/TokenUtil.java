package com.geek.geekstudio.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.geek.geekstudio.model.po.UserPO;

import java.util.Date;
/**
 * Token工具类
 */

public class TokenUtil {

    private static final String secret = "secret";
    private static final String Subject = "lab";

    public String create(UserPO userPO, Integer expireSeconds) {
        // TODO 改成使用数据库测试数据，结合Redis缓存用户信息？？？？
        return JWT.create()
                /*设置 载荷 Payload*/
//                .withIssuer(Issuer)//签名是有谁生成 例如 服务器
                .withSubject(Subject)//签名的主题
                .withAudience(userPO.getUserId() + "")//签名的观众 也可以理解谁接受签名的
//                .withIssuedAt(new Date()) //生成签名的时间
                .withExpiresAt(new Date(System.currentTimeMillis() + expireSeconds * 1000))//签名过期的时间

                /*签名 Signature */
                .sign(Algorithm.HMAC256(secret));
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

}
