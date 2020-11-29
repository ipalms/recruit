package com.geek.geekstudio.model.vo;

import com.geek.geekstudio.exception.ExceptionCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 每次操作的返回结果类型
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RestInfo {

    private int error_code;
    private String message;
    private Object data;

    public RestInfo(Object data){
        this.error_code = ExceptionCode.SUCCESS;
        this.message = "success";
        this.data = data;
    }

    //根据不同的参数重载成功、失败函数
    public static RestInfo build(int error_code,String message, Object data){
        return new RestInfo(error_code,message,data);
    }

    public static RestInfo success(){
        return new RestInfo(null);
    }

    public static RestInfo success(Object data){
        return new RestInfo(data);
    }

    public static RestInfo success(String message,Object data){
        return new RestInfo(ExceptionCode.SUCCESS,message,data);
    }

    public static RestInfo failed(){
        return new RestInfo(ExceptionCode.FAILED,null,null);
    }

    public static RestInfo failed(String message){
        return new RestInfo(ExceptionCode.FAILED,message,null);
    }

    public static RestInfo failed(int code,String message){
        return new RestInfo(code,message,null);
    }

    public static RestInfo failed(int code,String message,Object data){
        return new RestInfo(code,message,data);
    }

}
