package com.geek.geekstudio.service.proxy;

import com.geek.geekstudio.exception.ParameterError;
import com.geek.geekstudio.model.dto.TaskDTO;
import com.geek.geekstudio.model.vo.RestInfo;
import com.geek.geekstudio.service.TaskService;
import com.geek.geekstudio.service.impl.TaskServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TaskServiceProxy implements TaskService {

    @Autowired
    TaskServiceImpl taskService;

    /**
     *发布作业
     */
    @Override
    public RestInfo addTask(TaskDTO taskDTO) throws ParameterError {
        log.info("管理员编号为："+taskDTO.getAdminId()+" 布置方向编号为"+taskDTO.getCourseId()+"的作业");
        return taskService.addTask(taskDTO);
    }
}
