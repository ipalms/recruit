package com.geek.geekstudio.service.proxy;

import com.geek.geekstudio.exception.ParameterError;
import com.geek.geekstudio.exception.PermissionDeniedException;
import com.geek.geekstudio.exception.RecruitFileException;
import com.geek.geekstudio.model.dto.WorkDTO;
import com.geek.geekstudio.model.vo.RestInfo;
import com.geek.geekstudio.service.WorkService;
import com.geek.geekstudio.service.impl.WorkServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class WorkServiceProxy implements WorkService {

    @Autowired
    WorkServiceImpl workService;

    /**
     *学生提交作业
     */
    @Override
    public RestInfo addWork(WorkDTO workDTO) throws ParameterError {
        log.info("学生id为"+workDTO.getUserId()+" 提交了作业");
        return workService.addWork(workDTO);
    }

    /**
     * 删除整个的work及相关file记录
     */
    @Override
    public RestInfo deleteWork(WorkDTO workDTO) throws PermissionDeniedException {
        log.info("学生id为"+workDTO.getUserId()+" 尝试删除作业编号为"+workDTO.getId()+" 的作业");
        return workService.deleteWork(workDTO);
    }

    /**
     * 查看自己提交的某一方向所有作业记录
     */
    @Override
    public RestInfo queryAllMyWorks(int courseId, String userId, String baseUrl) {
        return workService.queryAllMyWorks(courseId,userId,baseUrl);
    }

    /**
     * 查看自己提交的某一方向的一项作业记录
     */
    @Override
    public RestInfo queryOneWork(int taskId, String userId, String baseUrl) {
        return workService.queryOneWork(taskId,userId,baseUrl);
    }
}
