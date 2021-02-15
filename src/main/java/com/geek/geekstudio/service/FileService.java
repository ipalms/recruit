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
    RestInfo articleFileUpload(int articleId, MultipartFile file) throws RecruitFileException;

    //多个文章文件上传
    RestInfo articleFilesUpload(int articleId, MultipartFile[] file);

    //task文件上传
    RestInfo taskFileUpload(int taskId, MultipartFile file) throws RecruitFileException;

    //多个task文件上传
    RestInfo taskFilesUpload(int taskId, MultipartFile[] files);

    //task文件的删除
    RestInfo delTaskFile(int id,String adminId) throws RecruitException;

    //work文件上传
    RestInfo workFileUpload(int workId, MultipartFile file) throws RecruitFileException, ParameterError;

    //多个work文件上传
    RestInfo workFilesUpload(int workId, MultipartFile[] files);

    //work文件的删除
    RestInfo delWorkFile(int id, String userId) throws RecruitException;
}
