package com.geek.geekstudio.exception;

/**
 * 参数传递错误异常类
 */
public class ParameterError extends RecruitException {

    public ParameterError(){
        super(ExceptionCode.Parameter_Error,"参数错误");
    }

    public ParameterError(String message){
        super(ExceptionCode.Parameter_Error,message);
    }

    public ParameterError(int code,String message){
        super(code,message);
    }

}
