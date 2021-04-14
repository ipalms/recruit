package com.geek.geekstudio.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.geek.geekstudio.annotaion.PassToken;
import com.geek.geekstudio.annotaion.UserLoginToken;
import com.geek.geekstudio.exception.*;
import com.geek.geekstudio.group.UserGroupValidated;
import com.geek.geekstudio.mapper.SuperAdminMapper;
import com.geek.geekstudio.mapper.UserMapper;
import com.geek.geekstudio.model.dto.DirectionDTO;
import com.geek.geekstudio.model.dto.LikeDTO;
import com.geek.geekstudio.model.dto.UserDTO;
import com.geek.geekstudio.model.po.AdminPO;
import com.geek.geekstudio.model.po.UserPO;
import com.geek.geekstudio.model.vo.RestInfo;
import com.geek.geekstudio.service.proxy.UserServiceProxy;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
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
    SuperAdminMapper adminMapper;

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
    public RestInfo checkStatus(@RequestBody UserDTO userDTO) throws UserRegisteredException {
        UserPO userPO = userMapper.queryUserByUserId(userDTO.getUserId());
        AdminPO adminPO=adminMapper.queryAdminByAdminId(userDTO.getUserId());
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
    public RestInfo checkEmail(@RequestBody UserDTO userDTO) throws UserRegisteredException {
        UserPO userPO = userMapper.queryUserByMail(userDTO.getMail());
        if(userPO!=null){
            //该邮箱已被注册
            throw new UserRegisteredException("该邮箱已被注册");
        }
        return RestInfo.success("该邮箱可以注册",null);
    }

    /**
     * 发送用户激活邮件 包含重复发送（携带用户ID存储redis判断）  用UserDTO封装（对于只有两个参数来说还可以用Map封装获得属性）
     * 复用到发送忘记密码邮件
     */
    @PassToken
    @PostMapping("/sendActiveMail")
    public RestInfo sendActiveMail(@RequestBody @Validated(UserGroupValidated.SendMailValidated.class) UserDTO userDTO)throws MessagingException{
        return userServiceProxy.sendActiveMail(userDTO.getUserId(),userDTO.getMail(),userDTO.getCodeType());
    }

    /**
     * 统一登录（管理员和新生） 用UserDTO封装（对于只有两个参数来说还可以用Map封装获得属性）
     */
    @PassToken
    @PostMapping("/login")
    public RestInfo login(@RequestBody @Validated(UserGroupValidated.LoginValidated.class) UserDTO userDTO) throws UsernameOrPasswordIncorrectException {
        //String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
        return userServiceProxy.login(userDTO.getUserId(),userDTO.getPassword());
    }

    /**
     * 改变用户信息的时候更新用户信息
     */
    @UserLoginToken
    @GetMapping("/updateInfo")
    public RestInfo updateInfo(HttpServletRequest request){
        //String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
        return userServiceProxy.updateInfo(request.getHeader("token"));
    }

    /**
     *注销--refreshToken可以使用JSONObject获得值，也可封装到对象中获取
     */
    @UserLoginToken
    @PostMapping("/logout")
    public RestInfo logout(@RequestBody UserDTO userDTO,HttpServletRequest request) throws NoTokenException {
        return userServiceProxy.logout(request.getHeader("token"),userDTO.getRefreshToken());
    }

    /**
     * 用户操作时token过期，根据refreshToken，为其再生成一个token
     */
    @PassToken
    @PostMapping("/resetToken")
    public RestInfo resetToken(@RequestBody UserDTO userDTO) throws RecruitException{
        return userServiceProxy.resetToken(userDTO.getRefreshToken());
    }

    /**
     * 用户修改密码
     */
    @UserLoginToken
    @PostMapping("/resetPassword")
    public RestInfo resetPassword(@RequestBody UserDTO userDTO,HttpServletRequest request) throws UsernameOrPasswordIncorrectException {
        UserPO userPO=userMapper.queryUserByUserIdAndPassword(userDTO.getUserId(),userDTO.getPassword());
        if(userPO==null){
            throw new UsernameOrPasswordIncorrectException("密码错误，请重新输入");
        }
        return userServiceProxy.resetPassword(userDTO,request.getHeader("token"));
    }

    /**
     * 用户忘记密码校验操作
     * 根据userId查询用户，判断其mail邮箱与所给的是相同
     */
    @PassToken
    @PostMapping("/checkUserLegality")
    public RestInfo checkUserLegality(@RequestBody UserDTO userDTO) throws ParameterError{
        return userServiceProxy.checkUserLegality(userDTO);
    }

    /**
     * 用户忘记密码-设置新密码
     */
    @PassToken
    @PostMapping("/findBackPassword")
    public RestInfo findBackPassword(@RequestBody UserDTO userDTO) throws EmailCodeWrongException{
        return userServiceProxy.findBackPassword(userDTO);
    }

    /**
     * 用户设置简介
     */
    @PassToken
    @PostMapping("/setIntroduce")
    public RestInfo setIntroduce(@RequestBody UserDTO userDTO){
        return userServiceProxy.setIntroduce(userDTO);
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

    /**
     * 查询当前登录用户是否接收日常邮箱
     */
    @UserLoginToken
    @GetMapping("/queryReceiveMailStatus")
    public RestInfo queryReceiveMailStatus(@RequestParam("userId") String userId){
        return userServiceProxy.queryReceiveMailStatus(userId);
    }

    /**
     * 改变接收日常邮件的状态
     */
    @UserLoginToken
    @PostMapping("/changeReceiveMailStatus")
    public RestInfo changeReceiveMailStatus(@RequestBody UserDTO userDTO){
        return userServiceProxy.changeReceiveMailStatus(userDTO.getUserId());
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
