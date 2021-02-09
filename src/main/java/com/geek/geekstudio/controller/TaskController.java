package com.geek.geekstudio.controller;

import com.geek.geekstudio.annotaion.AdminPermission;
import com.geek.geekstudio.annotaion.UserLoginToken;
import com.geek.geekstudio.exception.ParameterError;
import com.geek.geekstudio.model.dto.ArticleDTO;
import com.geek.geekstudio.model.dto.TaskDTO;
import com.geek.geekstudio.model.vo.RestInfo;
import com.geek.geekstudio.service.proxy.TaskServiceProxy;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public RestInfo addTask(@RequestBody TaskDTO taskDTO) throws ParameterError {
        return taskServiceProxy.addTask(taskDTO);
    }
}
