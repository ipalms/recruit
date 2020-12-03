package com.geek.geekstudio.service.proxy;

import com.geek.geekstudio.exception.*;
import com.geek.geekstudio.model.po.UserPO;
import com.geek.geekstudio.model.vo.RestInfo;
import com.geek.geekstudio.service.UserService;
import com.geek.geekstudio.service.impl.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;

/**
 * 用户服务静态代理类，用于降低耦合(可以顺便做日志 AOP操作等)
 */
@Service
@Slf4j
public class UserServiceProxy implements UserService {

    @Autowired
    UserServiceImpl userService;

    /**
     * 新生注册
     */
    @Override
    public RestInfo register(UserPO userPO,String activeCode) throws UserRegisteredException, EmailCodeWrongException {
        log.info("注册 userPO —— userId:"+userPO.getUserId()+" ,userName:"+userPO.getUserName()+
                " ,mail:"+userPO.getMail()+", password:"+userPO.getPassword()+" ,major:"+userPO.getMajor()+" ,activeCode:"+activeCode);
        return userService.register(userPO,activeCode);
    }
    /**
     *发送验证邮箱
     */
    @Override
    public RestInfo sendActiveMail(String userId,String mail) throws MessagingException {
        log.info("用户注册发送邮箱验证：userId:"+userId+",mail:"+mail);
        return userService.sendActiveMail(userId,mail);
    }
    /**
     * 统一登录（管理员和新生）
     */
    @Override
    public RestInfo login(String userId, String password) throws UsernameOrPasswordIncorrectException {
        log.info("用户登录：userId:"+userId+", password:"+password);
        return userService.login(userId,password);
    }

    /**
     *注销登录用户
     */
    @Override
    public RestInfo logout(String token) throws NoTokenException {
        return userService.logout(token);
    }

    /**
     * 用户操作时token过期，为其再生成一个token
     */
    @Override
    public RestInfo resetToken(String token) {
        log.info("用户操作中token过期："+token);
        return userService.resetToken(token);
    }



   /* @Override
    public RestInfo active(String activeCode) throws RecruitException {
        return userService.active(activeCode);
    }*/
}
