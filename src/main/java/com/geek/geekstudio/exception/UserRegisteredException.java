package com.geek.geekstudio.exception;

/**
 *此用户已注册
 */
public class UserRegisteredException extends RecruitException{

    public UserRegisteredException(){
        super(ExceptionCode.FAILED,"用户已注册");
    }

    public UserRegisteredException(String message){
        super(ExceptionCode.FAILED,message);
    }

    public UserRegisteredException(int code,String message){
        super(code,message);
    }


}
