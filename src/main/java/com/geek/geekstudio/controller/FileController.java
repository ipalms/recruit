package com.geek.geekstudio.controller;

import com.geek.geekstudio.annotaion.AdminPermission;
import com.geek.geekstudio.annotaion.UserLoginToken;
import com.geek.geekstudio.exception.ParameterError;
import com.geek.geekstudio.exception.RecruitException;
import com.geek.geekstudio.model.dto.TaskDTO;
import com.geek.geekstudio.model.dto.TaskFileDTO;
import com.geek.geekstudio.model.dto.WorkFileDTO;
import com.geek.geekstudio.model.vo.RestInfo;
import com.geek.geekstudio.service.proxy.FileServiceProxy;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Queue;

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
     *文章文件上传-- markdown
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

    /**
     * 传入参数:id、adminId --json格式 （发布者和超级管理员可以调用）
     * task文件的删除（目前仅删除taskfile相应的记录和磁盘中的文件）
     */
    @UserLoginToken
    @AdminPermission
    @PostMapping("/delTaskFile")
    public RestInfo delTaskFile(@RequestBody TaskFileDTO taskFileDTO) throws RecruitException{
        return fileServiceProxy.delTaskFile(taskFileDTO.getId(),taskFileDTO.getAdminId());
    }

    /**
     *上交作业的文件上传--work
     */
    @UserLoginToken
    @PostMapping("/workFileUpload")
    public RestInfo workFileUpload(int workId,
                                   @NotNull(message = "文件不能为null") MultipartFile file) throws RecruitException {
        return fileServiceProxy.workFileUpload(workId,file);
    }

    /**
     *多个作业的文件上传--work
     */
    @UserLoginToken
    @PostMapping("/workFilesUpload")
    public RestInfo workFilesUpload(int workId,
                                    @NotNull(message = "文件不能为null") MultipartFile[] files) throws RecruitException {
        return fileServiceProxy.workFilesUpload(workId,files);
    }

    /**
     * 传入参数:id、userId --json格式 （作业上传者可以调用）
     * work文件的删除（撤回上传的作业 撤回策略当没有一个对应的作业文件时删除作业的记录）
     */
    @UserLoginToken
    @PostMapping("/delWorkFile")
    public RestInfo delWorkFile(@RequestBody WorkFileDTO workFileDTO) throws RecruitException{
        return fileServiceProxy.delWorkFile(workFileDTO.getId(),workFileDTO.getUserId());
    }
}
