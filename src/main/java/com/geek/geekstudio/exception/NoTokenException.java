package com.geek.geekstudio.exception;


/**
 * token不存在异常类
 */

public class NoTokenException extends RecruitException {
    public NoTokenException() {
        super(ExceptionCode.TOKEN_MISS,"token不存在");
    }

    public NoTokenException(String message){
        super(ExceptionCode.TOKEN_MISS,message);
    }

    public NoTokenException(int code,String message){
        super(code,message);
    }
}
