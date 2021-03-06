package com.geek.geekstudio.service.impl;

import com.geek.geekstudio.exception.ExceptionCode;
import com.geek.geekstudio.model.vo.ErrorMsg;
import com.geek.geekstudio.service.JavaMailService;
import com.geek.geekstudio.util.UuidUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class JavaMailServiceImpl implements JavaMailService {
    private static String urlAddress="http://127.0.0.1";
    @Value("${spring.mail.username}")
    private String publicEmailAccount;
    @Autowired
    JavaMailSender javaMailSender;
    @Autowired
    RedisTemplate<Object,Object> redisTemplate;  //k-v都是对象的

    /**
     * 发送激活邮件 -- 发送找回密码邮件
     */
    @Override
    public void sendActiveMail(String userId, String mail,Integer codeType) throws MessagingException {
            String code= (String) redisTemplate.opsForValue().get("code-"+userId);
            if(code!=null){
                //删除掉之前的验证码
                redisTemplate.delete("code-"+userId);
            }
            //创建一个消息邮件
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
            String activeCode=UuidUtil.getActiveCode();
            //邮件设置  开启html支持
            if(codeType==1){
                helper.setSubject("极客&勤奋蜂招新注册激活邮件");
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
            redisTemplate.opsForValue().set("code-"+userId,activeCode,15, TimeUnit.MINUTES);
    }

    /*
    @Async("taskExecutor")// 指定线程池，也可以直接写@Async
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Future<String> sendDailyMail(String mail, String title, String text) throws Exception {
        //创建一个消息邮件
        try {
            //log.info("11111");
            //Thread.sleep(10000); //测试多线程发送消息用时
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
            helper.setSubject(title);
            helper.setText(text,true);
            helper.setFrom(publicEmailAccount);
            helper.setTo(mail);
            javaMailSender.send(mimeMessage);
        } catch (Exception e){
            throw new Exception();
        }
        return new AsyncResult<>("ok");
    }*/


    /**
     * 发送日常邮件
     * 使用异步CountDownLatch latch任务发送邮件（异步方法--实质开线程实现）
     */
    @Async("taskExecutor")// 指定线程池，也可以直接写@Async
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void sendDailyMail(String mail, String title, String text, List<ErrorMsg> errorList, CountDownLatch latch){
        //创建一个消息邮件
        try {
            //log.info("11111");
            //Thread.sleep(10000); //测试多线程发送消息用时
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
            helper.setSubject(title);
            helper.setText(text,true);
            helper.setFrom(publicEmailAccount);
            helper.setTo(mail);
            javaMailSender.send(mimeMessage);
        } catch (Exception e){
            //后台线程去填充任务执行结果成功失败内容
            errorList.add(new ErrorMsg(ExceptionCode.EMAIL_SEND_WRONG, "此次通知邮箱号为:"+mail+" 的邮箱发送通知失败!", e.getMessage()));
        }
        latch.countDown();
    }




    /**
     * 发送激活邮件（使用链接的形式激活）
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
