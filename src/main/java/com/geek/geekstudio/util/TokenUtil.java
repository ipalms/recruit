package com.geek.geekstudio.util;



import com.geek.geekstudio.model.po.UserPO;
import io.jsonwebtoken.*;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Token工具类
 */
@Component
public class TokenUtil {
    //密钥（盐）
    private static final  String JWT_SECRET="7786df7fc3a34e26a61c034d5ec8245d";

    /**
     * 创建jwt
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
     * 解密jwt
     * @param jwt token
     * @throws ExpiredJwtException  token过期
     */
    public static Claims parseJWT(String jwt) throws ExpiredJwtException{
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
