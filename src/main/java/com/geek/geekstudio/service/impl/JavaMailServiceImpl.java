package com.geek.geekstudio.service.impl;

import com.geek.geekstudio.service.JavaMailService;
import com.geek.geekstudio.util.UuidUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class JavaMailServiceImpl implements JavaMailService {
    private static String urlAddress="http://127.0.0.1";
    private static String publicEmailAccount="2534232295@qq.com";
    @Autowired
    JavaMailSender javaMailSender;
    @Autowired
    RedisTemplate<Object,Object> redisTemplate;  //k-v都是对象的

    /**
     * 发送激活邮件 -- 发送找回密码邮件
     */
    //回滚到没发送验证码
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void sendActiveMail(String userId, String mail,Integer codeType) throws MessagingException {
            String code= (String) redisTemplate.opsForValue().get(userId);
            if(code!=null){
                log.info("用户"+userId+":已经发送过验证码将删除第一次的验证码-->"+code);
                //删除掉之前的验证码
                redisTemplate.delete(userId);
            }
            //创建一个消息邮件
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
            String activeCode=UuidUtil.getActiveCode();
            //邮件设置  开启html支持
            if(codeType==1){
                helper.setSubject("极客网注册激活邮件");
                helper.setText("亲，您的验证码是："+activeCode+"，请尽快填写哦~~",true);
            }
            if(codeType==2){
                helper.setSubject("找回密码邮件");
                helper.setText("找回密码校验码为："+activeCode+"，请尽快填写哦~~",true);
            }
            helper.setFrom(publicEmailAccount);
            helper.setTo(mail);
            javaMailSender.send(mimeMessage);
            //将验证码存入redis 15分钟过期
            redisTemplate.opsForValue().set(userId,activeCode,15, TimeUnit.MINUTES);
    }









   /* *//**
     * 发送激活邮件（使用链接的形式激活）
     *//*
    @Override
    public void sendActiveMail(String mail, String activeCode) throws MessagingException {
            //创建一个复杂的消息邮件
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            //邮件设置  开启html支持
            helper.setSubject("激活邮件");
            helper.setText("<a href='"+urlAddress+"/user/active?activeCode="+activeCode+"'>点击激活 [极客招新网]</a>",true);
            helper.setFrom(publicEmailCount);
            helper.setTo(mail);
            javaMailSender.send(mimeMessage);
    }*/
}
