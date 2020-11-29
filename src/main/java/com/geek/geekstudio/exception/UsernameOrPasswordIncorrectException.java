package com.geek.geekstudio.exception;

/**
 * 用户名或者密码错误
 */
public class UsernameOrPasswordIncorrectException extends RecruitException{

    public UsernameOrPasswordIncorrectException(){
        super(ExceptionCode.USERNAME_OR_PASSWORD_WRONG,"用户名或者密码错误");
    }

    public UsernameOrPasswordIncorrectException(String message){
        super(ExceptionCode.USERNAME_OR_PASSWORD_WRONG,message);
    }

    public UsernameOrPasswordIncorrectException(int code,String message){
        super(code,message);
    }
}
