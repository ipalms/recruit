package com.geek.geekstudio.exception;

/**
 * 用户尚未激活异常类
 */
public class UserNotActiveException extends RecruitException {
    public UserNotActiveException(){
        super(ExceptionCode.USER_NO_ACTIVE,"用户尚未激活");
    }

    public UserNotActiveException(String message){
        super(ExceptionCode.USER_NO_ACTIVE,message);
    }

    public UserNotActiveException(int code, String message){
        super(code,message);
    }
}
