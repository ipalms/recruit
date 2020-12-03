package com.geek.geekstudio.service;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;

public interface JavaMailService {
    //发送激活邮件
    void sendActiveMail(String userId,String mail) throws MessagingException;
}
