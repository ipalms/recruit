package com.geek.geekstudio.exception;

/**
  *文件上传、下载、删除异常类
 */
public class RecruitFileException extends RecruitException {

    public RecruitFileException(){
        super(ExceptionCode.FILE_ERROR,"文件上传失败！");
    }

    public RecruitFileException(String message) {
        super(ExceptionCode.FILE_ERROR,message);
    }

    public RecruitFileException(int code, String message) {
        super(code,message);
    }
}

