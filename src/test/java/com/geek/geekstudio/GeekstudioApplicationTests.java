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
    public static void main(String[] args) {
        String id="dakmkda";
        String substring = id.substring(0, 4);
        Integer a=Integer.valueOf(substring);
        System.out.println(Integer.valueOf(substring));
        //substring
        //System.out.println(id.substring(0,4));
    }
}
