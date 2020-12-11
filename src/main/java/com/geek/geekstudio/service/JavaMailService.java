package com.geek.geekstudio.service;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;

public interface JavaMailService {
    //发送激活邮件  及找回密码邮件
    void sendActiveMail(String userId,String mail,Integer codeType) throws MessagingException;

    void sendDailyMail(String mail, String title, String text) throws MessagingException;
}
