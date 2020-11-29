package com.geek.geekstudio.handler;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.geek.geekstudio.exception.*;
import com.geek.geekstudio.model.vo.RestInfo;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.ConstraintViolationException;

/**
 *  全局异常处理
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 参数校验错误抛出的异常（JSR303 标准的系列注解校验注解以及@Validated）
     * @param violationException
     * @return
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    public RestInfo violationExceptionHandler(ConstraintViolationException violationException){
        return handleErrorInfo(ExceptionCode.Parameter_Error, violationException.getMessage());
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
        return handleErrorInfo(noUserException.getMessage());
    }

    /**
     * 用户已注册
     * @param userRegisteredException
     * @return
     */
    @ExceptionHandler(UserRegisteredException.class)
    @ResponseBody
    public RestInfo userRegisteredExceptionHandler(UserRegisteredException userRegisteredException){
        return handleErrorInfo(userRegisteredException.getMessage());
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
     * token过期处理
     * @param tokenExpiredException
     * @return
     */
    @ExceptionHandler(TokenExpiredException.class)
    @ResponseBody
    public RestInfo tokenExpiredExceptionHandler(TokenExpiredException tokenExpiredException){
        return handleErrorInfo(ExceptionCode.Permission_Denied, tokenExpiredException.getMessage());
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
     * 实验室系统错误
     * @param labException
     * @return
     */
    @ExceptionHandler(RecruitException.class)
    @ResponseBody
    public RestInfo labExceptionHandler(RecruitException labException){
        return handleErrorInfo(labException.getMessage());
    }


    /**
     * exception异常，用于捕获其他异常
     * @param exception
     * @return
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
