package com.geek.geekstudio.controller;

import com.geek.geekstudio.annotaion.AdminPermission;
import com.geek.geekstudio.annotaion.UserLoginToken;
import com.geek.geekstudio.exception.ParameterError;
import com.geek.geekstudio.exception.RecruitException;
import com.geek.geekstudio.model.vo.RestInfo;
import com.geek.geekstudio.service.proxy.FileServiceProxy;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@RestController
@Validated
@Data
@RequestMapping("/file")
public class FileController {

    @Autowired
    FileServiceProxy fileServiceProxy;

    /**
     * 管理员和大一同学头像上传 -- multipart/form-data
     */
    @UserLoginToken
    @PostMapping("/imageUpload")
    public RestInfo imageUpload(@NotBlank(message = "用户id不能为空") String userId,
                                @NotNull(message = "文件不能为null") MultipartFile file) throws RecruitException, ParameterError {
        return fileServiceProxy.imageUpload(userId,file);
    }

    /**
     *文章文件上传-- markdown   (也可图片)
     */
    @UserLoginToken
    @AdminPermission
    @PostMapping("/articleFileUpload")
    public RestInfo articleFileUpload(int articleId,
                                      @NotNull(message = "文件不能为null") MultipartFile file) throws RecruitException {
        return fileServiceProxy.articleFileUpload(articleId,file);
    }

    /**
     * 多个文章文件上传
     */
    @UserLoginToken
    @AdminPermission
    @PostMapping("/articleFilesUpload")
    public RestInfo articleFilesUpload(int articleId,
                                      @NotNull(message = "文件不能为null") MultipartFile[] file) throws RecruitException {
        return fileServiceProxy.articleFilesUpload(articleId,file);
    }

    /**
     *发布作业的文件上传--task
     */
    @UserLoginToken
    @AdminPermission
    @PostMapping("/taskFileUpload")
    public RestInfo taskFileUpload(int taskId,
                                   @NotNull(message = "文件不能为null") MultipartFile file) throws RecruitException {
        return fileServiceProxy.taskFileUpload(taskId,file);
    }

    /**
     *多个作业的文件上传--task
     */
    @UserLoginToken
    @AdminPermission
    @PostMapping("/taskFilesUpload")
    public RestInfo taskFilesUpload(int taskId,
                                    @NotNull(message = "文件不能为null") MultipartFile[] files) throws RecruitException {
        return fileServiceProxy.taskFilesUpload(taskId,files);
    }
}
