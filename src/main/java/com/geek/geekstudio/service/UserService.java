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
    RestInfo sendActiveMail(String userId,String mail,Integer codeType) throws  MessagingException;

    //登录
    RestInfo login(String userId, String password) throws UsernameOrPasswordIncorrectException;

    //注销用户
    RestInfo logout(String token, String refreshToken) throws NoTokenException;

    //过期重新生成token
    RestInfo resetToken(String refreshToken) throws PermissionDeniedException;

    //用户修改密码
    RestInfo resetPassword(UserDTO userDTO, String token);

    //用户忘记密码校验操作
    RestInfo checkUserLegality(UserDTO userDTO) throws ParameterError;

    //用户忘记密码-设置新密码
    RestInfo findBackPassword(UserDTO userDTO) throws EmailCodeWrongException;

    //设置简介
    RestInfo setIntroduce(UserDTO userDTO);

    //选方向
    RestInfo chooseCourse(DirectionDTO directionDTO) throws ParameterError;

    //撤销已选方向
    RestInfo delCourse(DirectionDTO directionDTO) throws ParameterError;;

    //查询用户是否接收日常邮件
    RestInfo queryReceiveMailStatus(String userId);

    //该变用户接收日常邮件状态
    RestInfo changeReceiveMailStatus(String userId);







/*    //激活（使用链接的形式）
    RestInfo active(String activeCode) throws RecruitException;*/
}
