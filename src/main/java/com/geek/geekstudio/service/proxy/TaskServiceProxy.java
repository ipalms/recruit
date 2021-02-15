package com.geek.geekstudio.service.proxy;

import com.geek.geekstudio.exception.ParameterError;
import com.geek.geekstudio.exception.PermissionDeniedException;
import com.geek.geekstudio.exception.RecruitFileException;
import com.geek.geekstudio.model.dto.TaskDTO;
import com.geek.geekstudio.model.dto.WorkDTO;
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

    /**
     *修改作业
     */
    @Override
    public RestInfo updateTask(TaskDTO taskDTO) throws ParameterError, PermissionDeniedException {
        log.info("管理员编号为："+taskDTO.getAdminId()+" 尝试修改作业编号为"+taskDTO.getId()+"的作业");
        return taskService.updateTask(taskDTO);
    }

    /**
     *手动关闭作业提交通道
     */
    @Override
    public RestInfo closeTask(TaskDTO taskDTO) throws PermissionDeniedException {
        log.info("管理员编号为："+taskDTO.getAdminId()+" 尝试关闭任务编号为"+taskDTO.getId()+"的提交通道");
        return taskService.closeTask(taskDTO);
    }

    /**
     * 删除该作业
     */
    @Override
    public RestInfo deleteTask(TaskDTO taskDTO) throws PermissionDeniedException {
        log.info("管理员编号为："+taskDTO.getAdminId()+" 尝试删除任务编号为"+taskDTO.getId()+"的任务");
        return taskService.deleteTask(taskDTO);
    }

    /**
     *查看发布的作业
     */
    @Override
    public RestInfo queryTasks(int courseId, String baseUrl) {
        return taskService.queryTasks(courseId,baseUrl);
    }

    /**
     *管理员查看自己发布的作业
     */
    @Override
    public RestInfo queryMyTasks(String adminId, String baseUrl) {
        return taskService.queryMyTasks(adminId,baseUrl);
    }

    /**
     *管理员查看某项作业提交作业名单及详细数据（分页）
     */
    @Override
    public RestInfo queryOneTask(int page, int rows, int taskId, String baseUrl) {
        return taskService.queryOneTask(page,rows,taskId,baseUrl);
    }

    /**
     *给某个作业打分（分值1~10）
     */
    @Override
    public RestInfo giveScore(WorkDTO workDTO) throws ParameterError {
        log.info("管理员给作业编号为"+workDTO.getId()+" 的作业打"+workDTO.getScore()+" 分");
        return taskService.giveScore(workDTO);
    }

    /**
     * 批下载一个任务对应的作业
     */
    @Override
    public RestInfo downloadWorks(WorkDTO workDTO, String baseUrl) throws ParameterError, RecruitFileException {
        log.info("管理员尝试下载taskId为"+workDTO.getTaskId()+" 的一些作业");
        return taskService.downloadWorks(workDTO,baseUrl);
    }

}
