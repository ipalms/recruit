package com.geek.geekstudio.group;

import org.springframework.validation.annotation.Validated;

/**
 * 用户类分组校验
 */
public class UserGroupValidated {
    /**
     * 注册校验分组
     */
     public interface RegisterValidated{}
    /**
     * 注册发送用户激活邮件分组
     */
    public interface SendMailValidated{}
    /**
     * 登录分组
     */
    public interface LoginValidated {}
}
