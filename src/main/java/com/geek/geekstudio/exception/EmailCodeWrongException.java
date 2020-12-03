package com.geek.geekstudio.exception;

//验证码有误异常类
public class EmailCodeWrongException extends RecruitException {
    public EmailCodeWrongException(){
        super(ExceptionCode.EMAIL_CODE_WRONG,"验证码空~请重新发送验证码");
    }

    public EmailCodeWrongException(String message){
        super(ExceptionCode.EMAIL_CODE_WRONG,message);
    }

    public EmailCodeWrongException(int code, String message){
        super(code,message);
    }
}
