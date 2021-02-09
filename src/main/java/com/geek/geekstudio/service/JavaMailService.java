package com.geek.geekstudio.service;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;

public interface JavaMailService {
    //发送激活邮件  及找回密码邮件
    void sendActiveMail(String userId,String mail,Integer codeType) throws MessagingException;

    //发送日常邮件 , CountDownLatch latch
    Future<String> sendDailyMail(String mail, String title, String text) throws Exception;
}
