package com.geek.geekstudio.controller;

import com.geek.geekstudio.annotaion.PassToken;
import com.geek.geekstudio.annotaion.UserLoginToken;
import com.geek.geekstudio.exception.*;
import com.geek.geekstudio.mapper.UserMapper;
import com.geek.geekstudio.model.po.UserPO;
import com.geek.geekstudio.model.vo.RestInfo;
import com.geek.geekstudio.service.proxy.UserServiceProxy;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 *  处理与用户表相关的请求（）
 */
@RestController
@Validated
@Data
public class UserController {
    @Autowired
    UserServiceProxy userServiceProxy;
    @Autowired
    UserMapper userMapper;

    /**
     * 新生注册
     */
    @PassToken
    @PostMapping("/user/register")
    public RestInfo register(@Valid UserPO userPO,@RequestParam("activeCode") @NotBlank String activeCode) throws UserRegisteredException, EmailCodeWrongException {
        return userServiceProxy.register(userPO,activeCode);
    }

    /**
     *先检验学号是否已经被注册(以便激活)
     */
    @PassToken
    @PostMapping("/user/checkUserId")
    public RestInfo checkStatus(@NotBlank(message ="用户ID不能为空") @Length(min = 5,max = 30) String userId) throws UserRegisteredException {
        UserPO userPO = userMapper.queryUserByUserId(userId);
        if(userPO!=null){
            //该学号已被注册
            throw new UserRegisteredException("学号已被注册");
        }
        return RestInfo.success("该学号可以注册",null);
    }

    /**
     *先检验邮箱是否已经被注册(以便激活)
     */
    @PassToken
    @PostMapping("/user/checkEmail")
    public RestInfo checkEmail(@NotBlank(message ="用户邮箱不能为空") @Email(message = "邮箱格式错误") String mail) throws UserRegisteredException {
        UserPO userPO = userMapper.queryUserByMail(mail);
        if(userPO!=null){
            //该邮箱已被注册
            throw new UserRegisteredException("该邮箱已被注册");
        }
        return RestInfo.success("该邮箱可以注册",null);
    }

    /**
     * 发送用户激活邮件 包含重复发送（携带用户ID存储redis判断）
     */
    @PassToken
    @PostMapping("/user/sendActiveMail")
    public RestInfo sendActiveMail(@NotBlank(message ="用户ID不能为空") @Length(min = 5,max = 30) String userId,
                                   @NotBlank(message ="用户邮箱不能为空") @Email(message = "邮箱格式错误") String mail)throws MessagingException{
        return userServiceProxy.sendActiveMail(userId,mail);
    }

    /**
     * 统一登录（管理员和新生）
     */
    @PassToken
    @PostMapping("/user/login")
    public RestInfo login(@NotBlank(message ="用户ID不能为空") @Length(min = 5,max = 30) String userId,
                          @NotBlank(message ="用户密码不能为空")  @Length(min = 3,max = 30) String password, HttpServletResponse response) throws UsernameOrPasswordIncorrectException {
        return userServiceProxy.login(userId,password);
    }

    /**
     *注销
     */
    @UserLoginToken
    @GetMapping("/user/logout")
    public RestInfo logout(HttpServletRequest request) throws NoTokenException {
        return userServiceProxy.logout(request.getHeader("token"));
    }

    /**
     * 用户操作时token过期，为其再生成一个token
     */
    @PassToken
    @GetMapping("/user/resetToken")
    public RestInfo resetToken(HttpServletRequest request) throws RecruitException{
        return userServiceProxy.resetToken(request.getHeader("token"));
    }


   /* *//**
     *激活用户（使用链接的形式）
     *//*
    @PassToken
    @PostMapping("/user/active")
    public RestInfo active(@RequestParam("activeCode") String activeCode) throws RecruitException {
      return userServiceProxy.active(activeCode);
    }*/

}
