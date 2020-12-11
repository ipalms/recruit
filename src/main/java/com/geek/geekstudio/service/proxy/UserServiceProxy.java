package com.geek.geekstudio.service.proxy;

import com.geek.geekstudio.exception.*;
import com.geek.geekstudio.model.dto.DirectionDTO;
import com.geek.geekstudio.model.dto.UserDTO;
import com.geek.geekstudio.model.po.UserPO;
import com.geek.geekstudio.model.vo.RestInfo;
import com.geek.geekstudio.service.UserService;
import com.geek.geekstudio.service.impl.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;

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
    public RestInfo register(UserDTO userDTO) throws UserRegisteredException, EmailCodeWrongException {
        log.info("注册 userDTO —— userId:"+userDTO.getUserId()+" ,userName:"+userDTO.getUserName()+
                " ,mail:"+userDTO.getMail()+", password:"+userDTO.getPassword()+" ,major:"+userDTO.getMajor()+" ,activeCode:"+userDTO.getActiveCode());
        return userService.register(userDTO);
    }

    /**
     *发送验证邮箱
     */
    @Override
    public RestInfo sendActiveMail(String userId,String mail,Integer codeType) throws MessagingException {
        log.info("向用户发送邮件：userId:"+userId+",mail:"+mail+",codeType:"+codeType);
        return userService.sendActiveMail(userId,mail,codeType);
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
    public RestInfo logout(String token, String refreshToken) throws NoTokenException {
        return userService.logout(token,refreshToken);
    }

    /**
     * 用户操作时token过期，为其再生成一个token
     */
    @Override
    public RestInfo resetToken(String refreshToken) {
        log.info("刷新token的refreshToken："+refreshToken);
        return userService.resetToken(refreshToken);
    }

    /**
     *用户修改密码
     */
    @Override
    public RestInfo resetPassword(UserDTO userDTO, String token) {
        log.info("用户修改密码：userId:"+userDTO.getUserId()+"修改密码, 新密码newPassword:"+userDTO.getNewPassword());
        return userService.resetPassword(userDTO,token);
    }

    @Override
    public RestInfo checkUserLegality(UserDTO userDTO) throws ParameterError {
        return userService.checkUserLegality(userDTO);
    }

    /**
     * 用户忘记密码-设置新密码
     */
    @Override
    public RestInfo findBackPassword(UserDTO userDTO) throws EmailCodeWrongException {
        log.info("用户忘记密码校验：userId:"+userDTO.getUserId()+", 新密码newPassword:"+userDTO.getNewPassword()+", 验证码code:"+userDTO.getActiveCode());
        return userService.findBackPassword(userDTO);
    }

    @Override
    public RestInfo setIntroduce(UserDTO userDTO) {
        return userService.setIntroduce(userDTO);
    }

    @Override
    public RestInfo chooseCourse(DirectionDTO directionDTO) throws ParameterError {
        return userService.chooseCourse(directionDTO);
    }

    @Override
    public RestInfo delCourse(DirectionDTO directionDTO) throws ParameterError {
        return userService.delCourse(directionDTO);
    }

    /**
     * 查询当前登录用户是否接收日常邮箱
     */
    @Override
    public RestInfo queryReceiveMailStatus(String userId) {
        return userService.queryReceiveMailStatus(userId);
    }

    /**
     * 改变接收日常邮件的状态
     */
    @Override
    public RestInfo changeReceiveMailStatus(String userId) {
        return userService.changeReceiveMailStatus(userId);
    }





   /* @Override
    public RestInfo active(String activeCode) throws RecruitException {
        return userService.active(activeCode);
    }*/
}
