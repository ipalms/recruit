package com.geek.geekstudio.service;

import com.geek.geekstudio.exception.*;
import com.geek.geekstudio.model.dto.DirectionDTO;
import com.geek.geekstudio.model.dto.UserDTO;
import com.geek.geekstudio.model.po.UserPO;
import com.geek.geekstudio.model.vo.RestInfo;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;

/**
 * 用户Service接口
 */
public interface UserService {
    //新生注册
    RestInfo register(UserDTO userDTO) throws UserRegisteredException, EmailCodeWrongException;

    //发送验证邮箱
    RestInfo sendActiveMail(String userId,String mail) throws  MessagingException;

    //登录
    RestInfo login(String userId, String password) throws UsernameOrPasswordIncorrectException;

    //注销用户
    RestInfo logout(String token, String refreshToken) throws NoTokenException;

    //过期重新生成token
    RestInfo resetToken(String refreshToken);

    //选方向
    RestInfo chooseCourse(DirectionDTO directionDTO) throws ParameterError;

    //撤销已选方向
    RestInfo delCourse(DirectionDTO directionDTO) throws ParameterError;;



/*    //激活（使用链接的形式）
    RestInfo active(String activeCode) throws RecruitException;*/
}
