package com.geek.geekstudio.service;

import com.geek.geekstudio.exception.ParameterError;
import com.geek.geekstudio.exception.PermissionDeniedException;
import com.geek.geekstudio.exception.RecruitFileException;
import com.geek.geekstudio.model.dto.WorkDTO;
import com.geek.geekstudio.model.vo.RestInfo;

public interface WorkService {

    //学生提交作业
    RestInfo addWork(WorkDTO workDTO) throws ParameterError;

    //删除整个的work及相关file记录
    RestInfo deleteWork(WorkDTO workDTO) throws PermissionDeniedException;

    //查看自己提交的某一方向所有作业记录
    RestInfo queryAllMyWorks(int courseId, String userId, String baseUrl);

    //查看自己提交的某一方向的一项作业记录
    RestInfo queryOneWork(int taskId, String userId, String baseUrl);
}
