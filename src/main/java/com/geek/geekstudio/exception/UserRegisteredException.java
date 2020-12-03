package com.geek.geekstudio.exception;

/**
 *此用户已注册
 */
public class UserRegisteredException extends RecruitException{

    public UserRegisteredException(){
        super(ExceptionCode.USER_EXIST,"用户已注册");
    }

    public UserRegisteredException(String message){
        super(ExceptionCode.USER_EXIST,message);
    }

    public UserRegisteredException(int code,String message){
        super(code,message);
    }
}
