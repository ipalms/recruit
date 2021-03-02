package com.geek.geekstudio.controller;

import com.geek.geekstudio.annotaion.AdminPermission;
import com.geek.geekstudio.annotaion.UserLoginToken;
import com.geek.geekstudio.exception.ParameterError;
import com.geek.geekstudio.exception.PermissionDeniedException;
import com.geek.geekstudio.model.dto.ArticleDTO;
import com.geek.geekstudio.model.dto.TaskDTO;
import com.geek.geekstudio.model.vo.RestInfo;
import com.geek.geekstudio.service.proxy.TaskServiceProxy;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@Validated
@Data
@RequestMapping("/task")
public class TaskController {
    @Autowired
    TaskServiceProxy taskServiceProxy;

    /**
     *发布作业
     */
    @UserLoginToken
    @AdminPermission
    @PostMapping("/addTask")
    public RestInfo addTask(@RequestBody @Validated TaskDTO taskDTO) throws ParameterError {
        return taskServiceProxy.addTask(taskDTO);
    }

    /**
     *修改作业（发布者和超级管理员可以）
     *需要提供参数1.id(自增的) 2.adminId
     *可选修改参数1.taskName 2.effectiveTime 3.commitLate 4.isClosed 5.weight 6.courseId
     *参数要全部提供
     */
    @UserLoginToken
    @AdminPermission
    @PostMapping("/updateTask")
    public RestInfo updateTask(@RequestBody @Validated TaskDTO taskDTO) throws ParameterError, PermissionDeniedException {
        return taskServiceProxy.updateTask(taskDTO);
    }

    /**
     *手动关闭作业提交通道（发布者和超级管理员可以）
     *需要提供参数1.id(自增的) 2.adminId
     */
    @UserLoginToken
    @AdminPermission
    @PostMapping("/closeTask")
    public RestInfo closeTask(@RequestBody TaskDTO taskDTO) throws  PermissionDeniedException {
        return taskServiceProxy.closeTask(taskDTO);
    }

    /**
     *删除该作业，删除的是task以及涉及的taskfile的所有东西（数据库记录+磁盘文件）（发布者和超级管理员可以）
     *需要提供参数1.id(自增的) 2.adminId
     * （特殊情况可能有提交的作业记录---write）
     */
    @UserLoginToken
    @AdminPermission
    @PostMapping("/deleteTask")
    public RestInfo deleteTask(@RequestBody TaskDTO taskDTO) throws PermissionDeniedException {
        return taskServiceProxy.deleteTask(taskDTO);
    }

    /**
     *查看发布的作业--暂时不考虑时间结点因素
     */
    @UserLoginToken
    @GetMapping("/queryTasks")
    public RestInfo queryTasks(@RequestParam(name = "courseId") int courseId,
                               HttpServletRequest request)  {
        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
        return taskServiceProxy.queryTasks(courseId,baseUrl);
    }

}
