package com.geek.geekstudio.service;

import com.geek.geekstudio.exception.ParameterError;
import com.geek.geekstudio.exception.PermissionDeniedException;
import com.geek.geekstudio.exception.RecruitFileException;
import com.geek.geekstudio.model.dto.TaskDTO;
import com.geek.geekstudio.model.dto.WorkDTO;
import com.geek.geekstudio.model.vo.RestInfo;

public interface TaskService {

    //发布作业
    RestInfo addTask(TaskDTO taskDTO) throws ParameterError;

    //修改作业
    RestInfo updateTask(TaskDTO taskDTO) throws ParameterError, PermissionDeniedException;

    //手动关闭作业提交通道
    RestInfo closeTask(TaskDTO taskDTO)  throws PermissionDeniedException ;

    //删除该作业
    RestInfo deleteTask(TaskDTO taskDTO) throws PermissionDeniedException;

    //查看作业
    RestInfo queryTasks(int courseId);

    //管理员查看自己发布的作业
    RestInfo queryMyTasks(String adminId);

    //管理员查看某项作业提交作业名单及详细数据
    RestInfo queryOneTask(int page, int rows, int taskId);

    //给某个作业打分（分值1~10）
    RestInfo giveScore(WorkDTO workDTO) throws ParameterError;

    //批下载一个任务对应的作业
    RestInfo downloadWorks(WorkDTO workDTO) throws ParameterError, RecruitFileException;
}
