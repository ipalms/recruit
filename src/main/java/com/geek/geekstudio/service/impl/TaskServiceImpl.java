package com.geek.geekstudio.service.impl;

import com.geek.geekstudio.exception.ParameterError;
import com.geek.geekstudio.mapper.TaskMapper;
import com.geek.geekstudio.model.dto.TaskDTO;
import com.geek.geekstudio.model.po.TaskPO;
import com.geek.geekstudio.model.vo.RestInfo;
import com.geek.geekstudio.service.TaskService;
import com.geek.geekstudio.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TaskServiceImpl implements TaskService {
    @Autowired
    TaskMapper taskMapper;


    //@Autowired


    /**
     *发布作业
     */
    @Override
    public RestInfo addTask(TaskDTO taskDTO) throws ParameterError {
        String addTime=null,closeTime=null;
        TaskPO taskPO=null;
        if(taskDTO.getEffectiveTime()<System.currentTimeMillis()){
            throw new ParameterError("截至日期设置错误");
        }else {
            addTime= DateUtil.creatDate();
            closeTime=DateUtil.timeStampToDate(taskDTO.getEffectiveTime());
        }
        taskPO=new TaskPO(taskDTO.getCourseId(),taskDTO.getAdminId(),taskDTO.getTaskName(),addTime,closeTime,taskDTO.getCommitLate(),taskDTO.getIsClosed(),taskDTO.getWeight());
        taskMapper.addTask(taskPO);
        /*if(taskDTO.getCommitLate()==null){
            taskDTO.setCommitLate(0);
        }
        if(taskDTO.getIsClosed()==null){
            taskDTO.setIsClosed(0);
        }*/
        return RestInfo.success("发布作业记录上传成功",taskPO.getId());
    }
}
