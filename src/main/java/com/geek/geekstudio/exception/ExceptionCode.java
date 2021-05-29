package com.geek.geekstudio.exception;

/**
 * 异常类型编号   均为常量
 */
public class ExceptionCode {

    //操作失败
    public static final int FAILED = 400;
    //操作成功
    public static final int SUCCESS = 200;
    //文件操作失败
    public static final int FILE_ERROR = 405;
    //用户已经存在--不能再注册
    public static final int USER_EXIST = 410;
    //用户权限不足
    public static final int Permission_Denied = 415;
    //参数错误
    public static final int Parameter_Error = 420;
    //token不存在
    public static final int TOKEN_MISS=425;
    //token过期
    public static final int TOKEN_EXPIRED=430;
    //refreshToken过期
    public static final int REFRESH_TOKEN_EXPIRED=432;
    //账号或密码错误
    public static final int USERNAME_OR_PASSWORD_WRONG=435;
    //新生还没有激活
    public static final int USER_NO_ACTIVE=440;
    //用户不存在
    public static final int NO_USER = 450;
    //邮件发送失败
    public static final int EMAIL_SEND_WRONG = 455;
    //验证码过期或不存在
    public static final int EMAIL_CODE_WRONG = 460;
    //提交作业时间关闭
    public static final int DELAY_SUBMIT = 465;
    //服务器内部错误
    public static final int Server_Internal_Error = 500;

}
