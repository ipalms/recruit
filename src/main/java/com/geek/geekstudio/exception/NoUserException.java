package com.geek.geekstudio.exception;

/**
 * 查寻不到此用户的异常
 */
public class NoUserException extends RecruitException {

    public NoUserException(){
        super(ExceptionCode.NO_USER,"查无此人");
    }

    public NoUserException(String message){
        super(ExceptionCode.NO_USER,message);
    }

    public NoUserException(int code,String message){
        super(code,message);
    }

}
