package com.geek.geekstudio.service;

import com.geek.geekstudio.exception.ParameterError;
import com.geek.geekstudio.exception.RecruitException;
import com.geek.geekstudio.exception.RecruitFileException;
import com.geek.geekstudio.model.vo.RestInfo;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    //头像上传
    RestInfo imageUpload(String userId, MultipartFile file) throws RecruitFileException, ParameterError;

    //文章文件上传-- markdown
    RestInfo articleFileUpload(String articleId, MultipartFile file) throws RecruitFileException;

    //多个文章文件上传
    RestInfo articleFilesUpload(String articleId, MultipartFile[] file);
}
