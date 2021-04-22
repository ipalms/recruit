package com.geek.geekstudio.handler;

import com.geek.geekstudio.exception.*;
import com.geek.geekstudio.model.vo.ErrorMsg;
import com.geek.geekstudio.model.vo.RestInfo;
import io.jsonwebtoken.ExpiredJwtException;
import org.apache.ibatis.exceptions.TooManyResultsException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.mail.MessagingException;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 *  全局异常处理  若方法仅返回json格式可以使用@ResrControllerAdvice 可以使用log记录异常
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 校验错误拦截处理（使用@Validated注解抛出）
     * 抛出的信息是List<ErrorMsg> 集合
     */
    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public RestInfo validationBodyException(MethodArgumentNotValidException methodArgumentNotValidException) {
        BindingResult result = methodArgumentNotValidException.getBindingResult();
        List<ErrorMsg> errorList = new ArrayList<>();
        if (result.hasErrors()) {
            List<ObjectError> errors = result.getAllErrors();
            errors.forEach(p -> {
                FieldError fieldError = (FieldError) p;
                errorList.add(new ErrorMsg(ExceptionCode.Parameter_Error, "填写的属性"+fieldError.getField()+"不合规范",fieldError.getDefaultMessage()));
            });
        }
        return handleErrorInfo(ExceptionCode.Parameter_Error, "参数有误",errorList);
    }

    /**
     * 插入过快导致
     */
    @ResponseBody
    @ExceptionHandler(TooManyResultsException.class)
    public RestInfo parameterTypeException(TooManyResultsException tooManyResultsException) {
        return handleErrorInfo(ExceptionCode.Parameter_Error, "请求次数太多，出现了一些错误！！");
    }


    /**
     * 参数类型转换错误
     */
    @ResponseBody
    @ExceptionHandler(HttpMessageConversionException.class)
    public RestInfo httpMessageConversionExceptionHandler(HttpMessageConversionException httpMessageConversionException) {
        return handleErrorInfo(ExceptionCode.Parameter_Error, httpMessageConversionException.getMessage());
    }

    /**
     * 参数校验错误抛出的异常（@Valid注解抛出）
     * @param violationException
     * @return
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    public RestInfo violationExceptionHandler(ConstraintViolationException violationException){
        return handleErrorInfo(ExceptionCode.Parameter_Error, violationException.getMessage());
    }

    /**
     *处理Get请求中 使用@Valid 验证路径中请求实体校验失败后抛出的异常
     */
    @ExceptionHandler(BindException.class)
    @ResponseBody
    public RestInfo BindExceptionHandler(BindException bindException) {
        return handleErrorInfo(ExceptionCode.Parameter_Error, bindException.getMessage());
    }

    /**
     * 参数错误
     * @param parameterError
     * @return
     */
    @ExceptionHandler(ParameterError.class)
    @ResponseBody
    public RestInfo parameterErrorHandler(ParameterError parameterError){
        return handleErrorInfo(ExceptionCode.Parameter_Error,parameterError.getMessage());
    }

    /**
     * 用户不存在
     * @param noUserException
     * @return
     */
    @ExceptionHandler(NoUserException.class)
    @ResponseBody
    public RestInfo noUserExceptionHandler(NoUserException noUserException){
        return handleErrorInfo(ExceptionCode.NO_USER,noUserException.getMessage());
    }

    /**
     * 用户已注册
     * @param userRegisteredException
     * @return
     */
    @ExceptionHandler(UserRegisteredException.class)
    @ResponseBody
    public RestInfo userRegisteredExceptionHandler(UserRegisteredException userRegisteredException){
        return handleErrorInfo(ExceptionCode.USER_EXIST,userRegisteredException.getMessage());
    }

    /**
     * 邮箱发送失败(非自己定义的异常类)
     * @return
     */
    @ExceptionHandler(MessagingException.class)
    @ResponseBody
    public RestInfo messagingExceptionHandler(MessagingException messagingException){
        return handleErrorInfo(ExceptionCode.EMAIL_SEND_WRONG,messagingException.getMessage());
    }

    /**
     * 验证码过期或不存在
     * @return
     */
    @ExceptionHandler(EmailCodeWrongException.class)
    @ResponseBody
    public RestInfo emailSendWrongExceptionHandler(EmailCodeWrongException emailCodeWrongException){
        return handleErrorInfo(ExceptionCode.EMAIL_CODE_WRONG,emailCodeWrongException.getMessage());
    }

    /**
     * 用户尚未激活
     * @param userNotActiveException
     * @return
     */
    @ExceptionHandler(UserNotActiveException.class)
    @ResponseBody
    public RestInfo userNotActiveExceptionHandler(UserNotActiveException userNotActiveException){
        return handleErrorInfo(ExceptionCode.USER_NO_ACTIVE,userNotActiveException.getMessage());
    }

    /**
     * token不存在
     * @param noTokenException
     * @return
     */
    @ExceptionHandler(NoTokenException.class)
    @ResponseBody
    public RestInfo noTokenExceptionHandler(NoTokenException noTokenException){
        return handleErrorInfo(ExceptionCode.TOKEN_MISS,noTokenException.getMessage());
    }

    /**
     * token过期处理  前端会重新请求一个token
     * @param expiredJwtException
     * @return
     */
    @ExceptionHandler(ExpiredJwtException.class)
    @ResponseBody
    public RestInfo tokenExpiredExceptionHandler(ExpiredJwtException expiredJwtException){
        if(expiredJwtException.getMessage().equals("token过期了")){
            return  handleErrorInfo(ExceptionCode.TOKEN_EXPIRED, expiredJwtException.getMessage());
        }
        return handleErrorInfo(ExceptionCode.REFRESH_TOKEN_EXPIRED, expiredJwtException.getMessage());
    }

    /**
     * 用户名或者密码错误
     * @param nameOrPasswordIncorrectException
     * @return
     */
    @ExceptionHandler(UsernameOrPasswordIncorrectException.class)
    @ResponseBody
    public RestInfo nameOrPasswordIncorrectExceptionHandler(UsernameOrPasswordIncorrectException nameOrPasswordIncorrectException){
        return handleErrorInfo(ExceptionCode.USERNAME_OR_PASSWORD_WRONG,nameOrPasswordIncorrectException.getMessage());
    }

    /**
     * 权限不足
     * @param permissionDeniedException
     * @return
     */
    @ExceptionHandler(PermissionDeniedException.class)
    @ResponseBody
    public RestInfo permissionDeniedExceptionHandler(PermissionDeniedException permissionDeniedException){
        return handleErrorInfo(ExceptionCode.Permission_Denied,permissionDeniedException.getMessage());
    }

    /**
     * 文件操作失败
     * @param recruitFileException
     * @return
     */
    @ExceptionHandler(RecruitFileException.class)
    @ResponseBody
    public RestInfo permissionDeniedExceptionHandler(RecruitFileException recruitFileException){
        return handleErrorInfo(ExceptionCode.FILE_ERROR,recruitFileException.getMessage());
    }

    /**
     * 服务器内部错误
     * @param serverInternalErrorException
     * @return
     */
    @ExceptionHandler(ServerInternalErrorException.class)
    @ResponseBody
    public RestInfo serverInternalErrorExceptionHandler(ServerInternalErrorException serverInternalErrorException){
        return handleErrorInfo(ExceptionCode.Server_Internal_Error,serverInternalErrorException.getMessage());
    }

    /**
     * 异常父类
     * @param labException
     * @return
     */
    @ExceptionHandler(RecruitException.class)
    @ResponseBody
    public RestInfo recruitExceptionHandler(RecruitException labException){
        return handleErrorInfo(labException.getMessage());
    }

/*    *//**
     * exception异常，用于捕获其他异常(未定义出来的) 捕捉以后不能查看异常所在
     * @return
     *//*
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public RestInfo exceptionHandler(Exception exception){
        return handleErrorInfo("error");
    }*/

    private RestInfo handleErrorInfo(String message) {
        RestInfo restInfo = RestInfo.failed(message);
        return restInfo;
    }

    private RestInfo handleErrorInfo(int code,String message) {
        RestInfo restInfo = RestInfo.failed(code,message);
        return restInfo;
    }

    private RestInfo handleErrorInfo(int code,String message,Object data) {
        RestInfo restInfo = RestInfo.failed(code,message,data);
        return restInfo;
    }
}
