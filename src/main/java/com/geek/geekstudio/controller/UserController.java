package com.geek.geekstudio.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.geek.geekstudio.annotaion.PassToken;
import com.geek.geekstudio.annotaion.UserLoginToken;
import com.geek.geekstudio.exception.*;
import com.geek.geekstudio.group.UserGroupValidated;
import com.geek.geekstudio.mapper.AdminMapper;
import com.geek.geekstudio.mapper.UserMapper;
import com.geek.geekstudio.model.dto.DirectionDTO;
import com.geek.geekstudio.model.dto.UserDTO;
import com.geek.geekstudio.model.po.AdminPO;
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
 *  处理与用户表相关的请求
 */
@RestController
@Validated
@Data
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserServiceProxy userServiceProxy;
    @Autowired
    UserMapper userMapper;
    @Autowired
    AdminMapper adminMapper;

    /**
     * 新生注册 统一json格式提交  用UserDTO封装  采用分组校验
     */
    @PassToken
    @PostMapping("/register")
    public RestInfo register(@RequestBody @Validated(UserGroupValidated.RegisterValidated.class) UserDTO userDTO) throws UserRegisteredException, EmailCodeWrongException {
        return userServiceProxy.register(userDTO);
    }

    /**
     *先检验学号是否已经被注册(以便激活)
     */
    @PassToken
    @PostMapping("/checkUserId")
    public RestInfo checkStatus(@RequestBody @NotBlank String userId) throws UserRegisteredException {
        //获得value值
        JSONObject json=JSON.parseObject(userId);
        String userID= (String) json.get("userId");
        UserPO userPO = userMapper.queryUserByUserId(userID);
        AdminPO adminPO=adminMapper.queryAdminByAdminId(userID);
        if(userPO!=null||adminPO!=null){
            //该学号已被注册
            throw new UserRegisteredException("学号已被注册");
        }
        return RestInfo.success("该学号可以注册",null);
    }

    /**
     *先检验邮箱是否已经被注册(以便激活)
     */
    @PassToken
    @PostMapping("/checkEmail")
    public RestInfo checkEmail(@RequestBody @NotBlank String mail) throws UserRegisteredException {
        JSONObject json=JSON.parseObject(mail);
        String email= (String) json.get("mail");
        UserPO userPO = userMapper.queryUserByMail(email);
        if(userPO!=null){
            //该邮箱已被注册
            throw new UserRegisteredException("该邮箱已被注册");
        }
        return RestInfo.success("该邮箱可以注册",null);
    }

    /**
     * 发送用户激活邮件 包含重复发送（携带用户ID存储redis判断）  用UserDTO封装（对于只有两个参数来说还可以用Map封装获得属性）
     */
    @PassToken
    @PostMapping("/sendActiveMail")
    public RestInfo sendActiveMail(@RequestBody @Validated(UserGroupValidated.SendMailValidated.class) UserDTO userDTO)throws MessagingException{
        return userServiceProxy.sendActiveMail(userDTO.getUserId(),userDTO.getMail());
    }

    /**
     * 统一登录（管理员和新生） 用UserDTO封装（对于只有两个参数来说还可以用Map封装获得属性）
     */
    @PassToken
    @PostMapping("/login")
    public RestInfo login(@RequestBody @Validated(UserGroupValidated.LoginValidated.class) UserDTO userDTO) throws UsernameOrPasswordIncorrectException {
        return userServiceProxy.login(userDTO.getUserId(),userDTO.getPassword());
    }

    /**
     *注销
     */
    @UserLoginToken
    @PostMapping("/logout")
    public RestInfo logout(@RequestBody String refreshToken,HttpServletRequest request) throws NoTokenException {
        JSONObject json=JSON.parseObject(refreshToken);
        String refresh_Token= (String) json.get("refreshToken");
        return userServiceProxy.logout(request.getHeader("token"),refresh_Token);
    }

    /**
     * 用户操作时token过期，根据refreshToken，为其再生成一个token
     */
    @PassToken
    @PostMapping("/resetToken")
    public RestInfo resetToken(@RequestBody String refreshToken) throws RecruitException{
        JSONObject json=JSON.parseObject(refreshToken);
        String refresh_Token= (String) json.get("refreshToken");
        return userServiceProxy.resetToken(refresh_Token);
    }

    /**
     *大一同学选择方向
     */
    @UserLoginToken
    @PostMapping("/chooseCourse")
    public RestInfo chooseCourse(@RequestBody @Validated DirectionDTO directionDTO) throws ParameterError{
        return userServiceProxy.chooseCourse(directionDTO);
    }

    /**
     *大一同学撤销选择方向
     */
    @UserLoginToken
    @PostMapping("/delCourse")
    public RestInfo delCourse(@RequestBody @Validated DirectionDTO directionDTO) throws ParameterError{
        return userServiceProxy.delCourse(directionDTO);
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
