package com.geek.geekstudio.service;

import com.geek.geekstudio.exception.ParameterError;
import com.geek.geekstudio.model.dto.TaskDTO;
import com.geek.geekstudio.model.vo.RestInfo;

public interface TaskService {

    //发布作业
    RestInfo addTask(TaskDTO taskDTO) throws ParameterError;
}
