package com.geek.geekstudio.controller;

import com.geek.geekstudio.annotaion.UserLoginToken;
import com.geek.geekstudio.exception.ParameterError;
import com.geek.geekstudio.exception.PermissionDeniedException;
import com.geek.geekstudio.exception.RecruitFileException;
import com.geek.geekstudio.model.dto.WorkDTO;
import com.geek.geekstudio.model.vo.RestInfo;
import com.geek.geekstudio.service.proxy.WorkServiceProxy;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@Validated
@Data
@RequestMapping("/work")
public class WorkController {

    @Autowired
    WorkServiceProxy workServiceProxy;

    /**
     *学生提交作业
     */
    @UserLoginToken
    @PostMapping("/addWork")
    public RestInfo addWork(@RequestBody WorkDTO workDTO) throws ParameterError {
        return workServiceProxy.addWork(workDTO);
    }

    /**
     * 删除整个的work及相关file记录
     * 参数：1.id 2.userId
     */
    @UserLoginToken
    @PostMapping("/deleteWork")
    public RestInfo deleteWork(@RequestBody WorkDTO workDTO) throws PermissionDeniedException {
        return workServiceProxy.deleteWork(workDTO);
    }

    /**
     * 查看自己提交的某一方向所有作业记录
     * 参数：1.courseId 2.userId
     */
    @UserLoginToken
    @GetMapping("/queryAllMyWorks")
    public RestInfo queryAllMyWorks(@RequestParam(name = "courseId") int courseId,
                                    @RequestParam(name = "userId") String userId,
                                    HttpServletRequest request) throws PermissionDeniedException {
        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":8080" + request.getContextPath();
        return workServiceProxy.queryAllMyWorks(courseId,userId,baseUrl);
    }

    /**
     * 查看自己提交的某一方向的一项作业记录
     * 参数：1.courseId 2.userId 3.taskId
     */
    @UserLoginToken
    @GetMapping("/queryOneWork")
    public RestInfo queryOneWork(@RequestParam(name = "taskId") int taskId,
                                 @RequestParam(name = "userId") String userId,
                                 HttpServletRequest request) {
        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":8080" + request.getContextPath();
        return workServiceProxy.queryOneWork(taskId,userId,baseUrl);
    }

}
