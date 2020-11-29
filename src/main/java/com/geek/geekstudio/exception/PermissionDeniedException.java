package com.geek.geekstudio.exception;


/**
 * 操作权限不足异常类
 */

public class PermissionDeniedException extends RecruitException {

    public PermissionDeniedException(){
        super(ExceptionCode.Permission_Denied,"权限不足");
    }

    public PermissionDeniedException(String message){
        super(ExceptionCode.Permission_Denied,message);
    }

    public PermissionDeniedException(int code,String message){
        super(code,message);
    }


}
