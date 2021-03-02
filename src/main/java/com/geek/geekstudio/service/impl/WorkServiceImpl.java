package com.geek.geekstudio.service.impl;

import com.geek.geekstudio.exception.ExceptionCode;
import com.geek.geekstudio.exception.ParameterError;
import com.geek.geekstudio.exception.PermissionDeniedException;
import com.geek.geekstudio.exception.RecruitFileException;
import com.geek.geekstudio.mapper.TaskMapper;
import com.geek.geekstudio.mapper.WorkFileMapper;
import com.geek.geekstudio.mapper.WorkMapper;
import com.geek.geekstudio.model.dto.WorkDTO;
import com.geek.geekstudio.model.po.TaskPO;
import com.geek.geekstudio.model.po.WorkFilePO;
import com.geek.geekstudio.model.po.WorkPO;
import com.geek.geekstudio.model.vo.RestInfo;
import com.geek.geekstudio.model.vo.WorkFileVO;
import com.geek.geekstudio.model.vo.WorkVO;
import com.geek.geekstudio.service.WorkService;
import com.geek.geekstudio.util.DateUtil;
import com.geek.geekstudio.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class WorkServiceImpl implements WorkService {

    @Autowired
    WorkMapper workMapper;

    @Autowired
    WorkFileMapper workFileMapper;

    @Autowired
    TaskMapper taskMapper;

    @Autowired
    FileUtil fileUtil;

    /**
     *学生提交作业
     */
    @Override
    public RestInfo addWork(WorkDTO workDTO) throws ParameterError {
        String currentTime= DateUtil.creatDate();
        TaskPO taskPO=taskMapper.queryOneTaskById(workDTO.getTaskId());
        if(taskPO==null){
            throw new ParameterError();
        }
        //判断两种作业提交通道已关闭情况，一是数据库字段已关闭二是数据库还没更新关闭字段的情况
        if((taskPO.getCommitLate()==0&&taskPO.getIsClosed()==1)||
                (taskPO.getCommitLate()==0&&taskPO.getIsClosed()==0&&(currentTime.compareTo(taskPO.getCloseTime())>0))){
            //是后者情况去修改数据库记录
            if(taskPO.getIsClosed()==0){
                taskMapper.shutUpCommit(taskPO.getId());
            }
            //log.info("学生id为"+workDTO.getUserId()+" 由于作业通道关闭提交作业id为"+workDTO.getTaskId()+" 的作业失败");
            return RestInfo.failed(ExceptionCode.DELAY_SUBMIT,"作业通道已关闭，不可提交作业！");
        }
        workDTO.setAddTime(currentTime);
        workMapper.addWork(workDTO);
        return  RestInfo.success("提交作业记录上传成功",workDTO.getId());
    }

    /**
     * 删除整个的work及相关file记录
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestInfo deleteWork(WorkDTO workDTO) throws PermissionDeniedException {
        int id=workDTO.getId();
        String userId=workDTO.getUserId();
        workDTO=workMapper.queryUserIdById(id);
        if(!(workDTO.getUserId()).equals(userId)){
            throw new PermissionDeniedException("非提交该作业学生，不可删除！");
        }
        workMapper.deleteByWorkId(id);
        workFileMapper.deleteFilesByWorkId(id);
        try {
            fileUtil.deleteDir(new File(fileUtil.buildWorkFilePath(workDTO.getCourseId(),workDTO.getUserId(),workDTO.getTaskId())));
        } catch (Exception e) {
            log.info("workID为"+id+" 文件夹删除文件失败");
        }
        return RestInfo.success("删除提交的作业成功",null);
    }

    /**
     * 查看自己提交的某一方向所有作业记录
     * 参数：1.courseId 2.userId
     */
    @Override
    public RestInfo queryAllMyWorks(int courseId, String userId, String baseUrl) {
        Map<String,Object> data=new HashMap<>();
        List<WorkFileVO> workFileVOList=null;
        List<WorkVO> workVOList=workMapper.queryWorkByCidAndUid(courseId,userId);
        int total=0;
        if(workVOList!=null){
            total=workVOList.size();
            for(WorkVO workVO:workVOList){
                workFileVOList=workFileMapper.queryFilesByWorkId(workVO.getId());
                if(workFileVOList!=null){
                    for(WorkFileVO workFileVO:workFileVOList){
                        workFileVO.setFilePath(fileUtil.getFileUrl(baseUrl, workFileVO.getFilePath()));
                    }
                }
                workVO.setWorkFileVOList(workFileVOList);
            }
        }
        data.put("total",total);
        data.put("taskPOList",workVOList);
        return RestInfo.success("查询所有提交作业成功",data);
    }

    /**
     * 查看自己提交的某一方向的一项作业记录
     */
    @Override
    public RestInfo queryOneWork(int taskId, String userId, String baseUrl) {
        List<WorkFileVO> workFileVOList=null;
        WorkVO workVO=workMapper.queryWorkByIDS(taskId,userId);
        if(workVO==null){
            return RestInfo.success("还没有提交作业，赶快提交吧！",null);
        }else {
            workFileVOList=workFileMapper.queryFilesByWorkId(workVO.getId());
            if(workFileVOList!=null){
                for(WorkFileVO workFileVO:workFileVOList){
                    workFileVO.setFilePath(fileUtil.getFileUrl(baseUrl, workFileVO.getFilePath()));
                }
            }
            workVO.setWorkFileVOList(workFileVOList);
        }
        return RestInfo.success("查询一项作业记录成功",workVO);
    }
}
