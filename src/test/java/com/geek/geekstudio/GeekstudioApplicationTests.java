package com.geek.geekstudio;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.geek.geekstudio.mapper.UserMapper;
import com.geek.geekstudio.model.po.UserPO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class GeekstudioApplicationTests {
    @Autowired
    RedisTemplate<Object, Object> redisTemplate;  //k-v都是对象的
    @Autowired
    UserMapper userMapper;
    @Autowired
    JavaMailSender javaMailSender;

    @Test
    void contextLoads() {
        redisTemplate.opsForValue().set("key", "yes ok", 2, TimeUnit.SECONDS);
        System.out.println(redisTemplate.opsForValue().get("key"));
    }

    @Test
    void testMapper() {
        UserPO userPO = userMapper.queryUserByUserId("2019211399");
        System.out.println(userPO);
    }

    @Test
    void testEmail() throws MessagingException {
        //创建一个复杂的消息邮件
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        //邮件设置  开启html支持
        helper.setSubject("激活邮件");
        helper.setText("<a href='http://127.0.0.1/user/active?activeCode=" + "xajsdidmasldik" + "'>点击激活 [极客招新网]</a>", true);
        helper.setFrom("2534232295@qq.com");
        helper.setTo("wangjiahui20011014@163.com");
        javaMailSender.send(mimeMessage);
    }

    @Test
    void testJSON() {
        String a = "{'userId':'superAdmin'}";
        JSONObject json = JSON.parseObject(a);
        for (Map.Entry<String, Object> b : json.entrySet()) {
            System.out.println();
        }
        json.entrySet();
        String refresh_Token = (String) json.get("");
    }

    @Test
    void testWrite() {
        String path = "D:\\all\\article\\9\\SpringBoot会用到的注解.md";
        try {
            // default StandardCharsets.UTF_8
            //将文章markdown内容转成字符串
            String content = Files.readString(Paths.get(path));
            System.out.println(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
