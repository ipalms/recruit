package com.geek.geekstudio;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

@SpringBootTest
class GeekstudioApplicationTests {
    @Autowired
    RedisTemplate<Object,Object> redisTemplate;  //k-v都是对象的
    @Test
    void contextLoads()  {
            redisTemplate.opsForValue().set("key","yes ok",2, TimeUnit.SECONDS);
            System.out.println(redisTemplate.opsForValue().get("key"));
    }
}
