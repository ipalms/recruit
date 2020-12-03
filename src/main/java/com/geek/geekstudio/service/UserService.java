package com.geek.geekstudio.service;

import com.geek.geekstudio.exception.*;
import com.geek.geekstudio.model.po.UserPO;
import com.geek.geekstudio.model.vo.RestInfo;

import javax.mail.MessagingException;

/**
 * 用户Service接口
 */
public interface UserService {
    //新生注册
    RestInfo register(UserPO userPO,String activeCode) throws UserRegisteredException, EmailCodeWrongException;

    //发送验证邮箱
    RestInfo sendActiveMail(String userId,String mail) throws  MessagingException;

    //登录
    RestInfo login(String userId, String password) throws UsernameOrPasswordIncorrectException;

    //注销用户
    RestInfo logout(String token) throws NoTokenException;

    RestInfo resetToken(String token);



/*    //激活（使用链接的形式）
    RestInfo active(String activeCode) throws RecruitException;*/
}
