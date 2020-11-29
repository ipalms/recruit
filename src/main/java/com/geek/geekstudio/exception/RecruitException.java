package com.geek.geekstudio.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 自定义异常基类,招新系统异常
 */
@Data
@AllArgsConstructor
public class RecruitException extends Exception {
    private int code=-1;   //默认的异常code值为-1
    private String message;

    public RecruitException(){
        this.message = "error";
    }

    public RecruitException(String message){
        this.message = message;
    }

    public RecruitException(Integer code, String message){
        this.code = code;
        this.message = message;
    }
}
