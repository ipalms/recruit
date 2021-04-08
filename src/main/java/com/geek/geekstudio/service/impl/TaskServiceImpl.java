package com.geek.geekstudio.service.impl;

import com.geek.geekstudio.exception.ParameterError;
import com.geek.geekstudio.exception.PermissionDeniedException;
import com.geek.geekstudio.exception.RecruitFileException;
import com.geek.geekstudio.mapper.*;
import com.geek.geekstudio.model.dto.TaskDTO;
import com.geek.geekstudio.model.dto.WorkDTO;
import com.geek.geekstudio.model.po.TaskPO;
import com.geek.geekstudio.model.po.UserPO;
import com.geek.geekstudio.model.vo.*;
import com.geek.geekstudio.service.TaskService;
import com.geek.geekstudio.util.DateUtil;
import com.geek.geekstudio.util.DozerUtil;
import com.geek.geekstudio.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class TaskServiceImpl implements TaskService {
    @Autowired
    TaskMapper taskMapper;

    @Autowired
    TaskFileMapper taskFileMapper;

    @Autowired
    WorkMapper workMapper;

    @Autowired
    WorkFileMapper workFileMapper;

    @Autowired
    AdminMapper adminMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    FileUtil fileUtil;

    private static final String times = "0 1 0 * * ?";

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
        return RestInfo.success("发布作业记录上传成功",taskPO.getId());
    }

    /**
     *修改作业
     */
    @Override
    //@Transactional(rollbackFor = Exception.class)
    public RestInfo updateTask(TaskDTO taskDTO) throws ParameterError, PermissionDeniedException {
        String closeTime=null;
        if(!(taskMapper.queryAdminIdById(taskDTO.getId())).equals(taskDTO.getAdminId())
                &&!("super".equals(adminMapper.queryTypeById(taskDTO.getAdminId())))){
            throw new PermissionDeniedException("非发布该作业管理员，不可修改！");
        }
        if(taskDTO.getEffectiveTime()!=0&&taskDTO.getEffectiveTime()<System.currentTimeMillis()){
            throw new ParameterError("截至日期设置错误");
        }else {
            closeTime=DateUtil.timeStampToDate(taskDTO.getEffectiveTime());
        }
        taskMapper.updateTask(taskDTO.getId(),taskDTO.getCourseId(),taskDTO.getTaskName(),closeTime,taskDTO.getCommitLate(),taskDTO.getIsClosed(),taskDTO.getWeight());
        return RestInfo.success("修改作业成功",null);
    }

    /**
     *手动关闭作业提交通道
     */
    @Override
    public RestInfo closeTask(TaskDTO taskDTO) throws PermissionDeniedException {
        if(!(taskMapper.queryAdminIdById(taskDTO.getId())).equals(taskDTO.getAdminId())
                &&!("super".equals(adminMapper.queryTypeById(taskDTO.getAdminId())))){
            throw new PermissionDeniedException("非发布该作业管理员，不可修改！");
        }
        taskMapper.closeTask(taskDTO.getId());
        return RestInfo.success("手动关闭作业提交通道成功",null);
    }

    /**
     * 删除该作业
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestInfo deleteTask(TaskDTO taskDTO) throws PermissionDeniedException {
        int id=taskDTO.getId();
        String adminId=taskDTO.getAdminId();
        taskDTO=taskMapper.queryAdminIdById(id);
        if(!(taskDTO.getAdminId()).equals(adminId)
                &&!("super".equals(adminMapper.queryTypeById(adminId)))){
            throw new PermissionDeniedException("非发布该作业管理员，不可删除！");
        }
        int courseId=taskDTO.getCourseId();
        taskMapper.deleteByTaskId(id);
        taskFileMapper.deleteFilesByTaskId(id);
        try {
            fileUtil.deleteDir(new File(fileUtil.buildTaskFilePath(taskDTO.getCourseId(),id)));
        } catch (Exception e) {
            log.info("taskID为"+id+" 文件夹删除文件失败");
        }
        List<WorkVO> workVOList=workMapper.queryWorkByTaskId(id);
        if(workVOList!=null){
            try {
                fileUtil.deleteDir(new File(fileUtil.getAllWork(courseId,id)));
            } catch (Exception e) {
                log.info("workID为"+id+" 文件夹删除文件失败");
            }
            for(WorkVO workVO:workVOList){
                workFileMapper.deleteFilesByWorkId(workVO.getId());
                workMapper.deleteByWorkId(workVO.getId());
            }
        }
        return RestInfo.success("删除发布的作业成功",null);
    }

    /**
     *查看发布的作业
     */
    @Override
    public RestInfo queryTasks(int courseId, String baseUrl) {
        Map<String,Object> data=new HashMap<>();
        List<TaskFileVO> taskFileVOList=null;
        List <TaskVO> taskPOList=taskMapper.queryTasksByCourseId(courseId);
        int total=0;
        if(taskPOList!=null){
            total=taskPOList.size();
            for(TaskVO taskVO:taskPOList){
                taskFileVOList=taskFileMapper.queryTaskFileById(taskVO.getId());
                if(taskFileVOList!=null) {
                    for (TaskFileVO taskFileVO : taskFileVOList) {
                        taskFileVO.setFilePath(fileUtil.getFileUrl(baseUrl, taskFileVO.getFilePath()));
                    }
                }
                taskVO.setTaskFileVOList(taskFileVOList);
            }
        }
        data.put("total",total);
        data.put("taskPOList",taskPOList);
        return RestInfo.success("查询作业成功",data);
    }

    /**
     *管理员查看自己发布的作业
     */
    @Override
    public RestInfo queryMyTasks(String adminId, String baseUrl) {
        Map<String,Object> data=new HashMap<>();
        List<TaskFileVO> taskFileVOList=null;
        List <TaskVO> taskPOList=taskMapper.queryTasksByAdminId(adminId);
        int total=0;
        if(taskPOList!=null){
            total=taskPOList.size();
            for(TaskVO taskVO:taskPOList){
                taskFileVOList=taskFileMapper.queryTaskFileById(taskVO.getId());
                if(taskFileVOList!=null) {
                    for (TaskFileVO taskFileVO : taskFileVOList) {
                        taskFileVO.setFilePath(fileUtil.getFileUrl(baseUrl, taskFileVO.getFilePath()));
                    }
                }
                taskVO.setTaskFileVOList(taskFileVOList);
            }
        }
        data.put("total",total);
        data.put("taskPOList",taskPOList);
        return RestInfo.success("查询作业成功",data);
    }

    /**
     *管理员查看某项作业提交作业名单及详细数据（分页）
     */
    @Override
    public RestInfo queryOneTask(int page, int rows, int taskId, String baseUrl) {
        int total=workMapper.querySubmitTotal(taskId);
        int start=(page-1)*rows;
        List<WorkFileVO> workFileVOList=null;
        List<WorkVO> workVOList=workMapper.queryWorksByTaskId(taskId,start,rows);
        if(workVOList!=null){
            for(WorkVO workVO:workVOList){
                UserPO userPO = userMapper.queryUserByUserId(workVO.getUserId());
                if(userPO!=null){
                    UserVO userVO = DozerUtil.getDozerBeanMapper().map(userPO,UserVO.class);
                    workVO.setUserVO(userVO);
                }
                workFileVOList=workFileMapper.queryFilesByWorkId(workVO.getId());
                if(workFileVOList!=null){
                    for(WorkFileVO workFileVO:workFileVOList){
                        workFileVO.setFilePath(fileUtil.getFileUrl(baseUrl, workFileVO.getFilePath()));
                    }
                }
                workVO.setWorkFileVOList(workFileVOList);
            }
        }
        int totalPage=total%rows==0?total/rows:total/rows+1;
        return RestInfo.success(new PageListVO(total,page,totalPage,rows,workVOList));
    }

    /**
     *给某个作业打分（分值1~10）
     */
    @Override
    public RestInfo giveScore(WorkDTO workDTO) throws ParameterError {
        if(workDTO.getScore()!=null&&(workDTO.getScore()>10||workDTO.getScore()<1)){
            throw new ParameterError("给出的分数不合规！");
        }
        workMapper.updateScore(workDTO.getId(),workDTO.getScore());
        if(workDTO.getScore()==null){
            return RestInfo.success("撤销已给分数成功",null);
        }
        return RestInfo.success("打分成功",null);
    }

    /**
     * 批下载一个任务对应的作业
     */
    @Override
    public RestInfo downloadWorks(WorkDTO workDTO, String baseUrl) throws ParameterError, RecruitFileException {
        TaskPO taskPO = taskMapper.queryOneTaskById(workDTO.getTaskId());
        if(taskPO==null){
            throw new ParameterError();
        }
        String zipPath="/zip/"+taskPO.getCourseId()+"/"+taskPO.getId() + "/" + taskPO.getTaskName() + ".zip";
        //log.info(zipPath);
        String[] pathList;
        int courseId=taskPO.getCourseId();
        int taskId=taskPO.getId();
        if(workDTO.getUserIdList()==null){
            pathList=new String[1];
            pathList[0]=fileUtil.getAllWork(courseId,taskId);
        }else {
            int i=0;
            List<String> userIdList=workDTO.getUserIdList();
            pathList=new String[userIdList.size()];
            for(String userId:userIdList){
                String path=fileUtil.buildWorkFilePath(courseId,userId,taskId);
                pathList[i++]=path;
            }
        }
        try {
            fileUtil.zipFileByPathList(pathList,fileUtil.buildPath(zipPath));
        } catch (IOException e) {
            throw new RecruitFileException("作业文件合并出错，请重试！");
        }
        String url = fileUtil.getFileUrl(baseUrl,zipPath);
        return RestInfo.success(url);
    }


    /**
     * 考虑每晚进行一次gc(垃圾回收)----
     * 定时任务，每周日凌晨2自动扫描任务查看其提交通道是否应该关闭
     * 0 0 2 ? * 1   每周日凌晨2点执行一次
     * 0 1 0 * * ?    每天晚上12：01分执行一次
     * 0 0/1 * * * ?   每分钟一次
     */
    //没有@Async时，若定义间隔很短内重复多次定时任务可能不会按时执行，因为只有单线程要等到上一个任务执行完才执行下一个
    //异步执行，线程之间不会互相干扰，任务自动提交到线程池
    @Async("taskExecutor")
    @Scheduled(cron = times)
    public void checkCloseTask(){
        log.info("定时任务--关闭已经超时的提交作业通道");
        List<TaskPO> taskPOList=taskMapper.queryTaskList(DateUtil.creatDate());
        if(taskPOList!=null){
            for(TaskPO taskPO:taskPOList){
                taskMapper.shutUpCommit(taskPO.getId());
            }
        }
    }
}
