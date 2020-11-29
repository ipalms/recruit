package com.geek.geekstudio.exception;

/**
 * 服务器内部异常类
 */
public class ServerInternalErrorException extends RecruitException {

    public ServerInternalErrorException(){
        super(ExceptionCode.Server_Internal_Error,"服务器内部异常");
    }

    public ServerInternalErrorException(String message){
        super(ExceptionCode.Server_Internal_Error,message);
    }

    public ServerInternalErrorException(int code,String message){
        super(code,message);
    }

}
