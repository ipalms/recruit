package com.geek.geekstudio.exception;

/**
 * 异常类型编号   均为常量
 */
public class ExceptionCode {

    //操作失败
    public static final int FAILED = -1;
    //操作成功
    public static final int SUCCESS = 0;
    //文件上传失败
    public static final int FILE_ERROR = 405;
    //用户权限不足
    public static final int Permission_Denied = 410;
    //参数错误
    public static final int Parameter_Error = 415;
    //token不存在
    public static final int TOKEN_MISS=420;
    //账号或密码错误
    public static final int USERNAME_OR_PASSWORD_WRONG=425;
    //密码过于简单，重设密码
    public static final int RESET_PASSWORD = 430;

    //服务器内部错误
    public static final int Server_Internal_Error = 500;

}
