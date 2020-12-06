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

    @Override
    public RestInfo chooseCourse(DirectionDTO directionDTO) throws ParameterError {
        return userService.chooseCourse(directionDTO);
    }

    @Override
    public RestInfo delCourse(DirectionDTO directionDTO) throws ParameterError {
        return userService.delCourse(directionDTO);
    }



   /* @Override
    public RestInfo active(String activeCode) throws RecruitException {
        return userService.active(activeCode);
    }*/
}
