package com.geek.geekstudio.util;



import io.jsonwebtoken.*;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Token工具类 Json Web Token(JWT)有两种是具体实现，分别时JWS与JWE，其中jws是我们常用的概念
 * token相当于是有时限的密码，而普通的用户密码的时限是由用户自己决定（用户改密）
 * --使用token可以作为身份鉴别、权限的管理的钥匙去访问API
 * --token是无状态的，用户拿到了token就可以进行这个token所标注能使用的所有权限
 * --所以jwt不可以识别token是否被劫持（冒用了），不管谁持有系统办法的正常token在使用jwt解密时都会正常通过（除非过期了）
 *    --可以使用https进行通信加密防止token被劫持
 * --但是jwt可以去鉴别token是否被篡改过（比如改失效时间），篡改的token在使用jwt解密时会抛出异常
 * --如何防止篡改：
 *    --Header：对TokenUtil.header（含有加密算法）进行Base64Url编码得到jwt的第一部分；
 *    --Payload：存放有效信息的地方（Claims部分），Base64Url编码得到第二部分；
 *    --Signature：是整个数据的认证信息。一般根据前两步的数据（Header、Payload）并通过header中声明的加密方式进行加盐secret组合加密，然后就构成了jwt的第3部分
 *    --服务端验证的时候只需要再执行一次 signature = 加密算法(header+"."+payload, 密钥)--然后对比与原token的signature是否一致，如果一致则说明没有被篡改。
 * --当然也可以配合内存组件维护有效token的（维护token黑白名单、redis）
 */
@Component
public class TokenUtil {
    //密钥（盐）
    private static final  String JWT_SECRET="7786df7fc3a34e26a61c034d5ec8245d";

    /**
     * 创建jwt
     * 方法列表可以再加一个字段标识是否是refreshToken 是的话在生成token的载荷信息多加入一个标识
     * 这样起到refreshToken不能用于登录只能用作刷新token的作用
     * @param subject 签收主体-userID
     * @param ttlMillis 过期的时间长度
     * @param type 对象的类型
     */
    public static String createJWT(String subject,String type, long ttlMillis) {
        //指定签名的时候使用的签名算法--header那部分
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        //生成JWT的时间
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        //创建payload的私有声明
        Map<String, Object> claims = new HashMap<>();
        claims.put("type", type);
        //生成签名的时候使用的秘钥secret,这个方法本地封装了的
        SecretKey key = generalKey();
        //下面就是在为payload添加各种标准声明和私有声明了
        JwtBuilder builder = Jwts.builder() //这里其实就是new一个JwtBuilder，设置jwt的body
                .setClaims(claims)          //如果有私有声明，一定要先设置这个自己创建的私有的声明，这个是给builder的claim赋值，一旦写在标准的声明赋值之后，就是覆盖了那些标准的声明的
                .setIssuedAt(now)           //iat: jwt的签发时间
                .setSubject(subject)        //sub(Subject)：代表这个JWT的主体，即它的所有人，这个是一个json格式的字符串，可以存放什么userId作为什么用户的唯一标志。
                .signWith(signatureAlgorithm, key);//设置签名使用的签名算法和签名使用的秘钥
        if (ttlMillis >= 0) {
            long expMillis = nowMillis + ttlMillis;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);     //设置过期时间
        }
        return builder.compact();           //压缩为jwt
    }

    /**
     * 解密jwt--可能会抛出很多种异常
     * @param jwt token
     * @throws ExpiredJwtException  token过期--能被解析，但是检验时效时已过期抛出
     * @throws SignatureException  签名异常
     * @throws MalformedJwtException  jwt格式不正确
     */
    public static Claims parseJWT(String jwt) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException{
        SecretKey key = generalKey();  //签名秘钥，和生成的签名的秘钥一模一样
        return Jwts.parser()  //得到DefaultJwtParser
                .setSigningKey(key)         //设置签名的秘钥
                .parseClaimsJws(jwt).getBody();
    }

    /**
     * 由字符串生成加密key
     */
    public static SecretKey generalKey(){
        //String stringKey = Constant.JWT_SECRET;//本地配置文件中加密的密文
        byte[] encodedKey = Base64.decodeBase64(JWT_SECRET);
        return new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
    }
}
