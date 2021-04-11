package com.geek.geekstudio.service.proxy;

import com.geek.geekstudio.exception.ParameterError;
import com.geek.geekstudio.exception.RecruitException;
import com.geek.geekstudio.exception.RecruitFileException;
import com.geek.geekstudio.mapper.*;
import com.geek.geekstudio.model.po.TaskPO;
import com.geek.geekstudio.model.po.WorkPO;
import com.geek.geekstudio.model.vo.ErrorMsg;
import com.geek.geekstudio.model.vo.RestInfo;
import com.geek.geekstudio.service.FileService;
import com.geek.geekstudio.service.impl.FileServiceImpl;
import com.geek.geekstudio.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class FileServiceProxy implements FileService {

    @Autowired
    FileServiceImpl fileService;

    @Autowired
    TaskFileMapper taskFileMapper;

    @Autowired
    TaskMapper taskMapper;

    @Autowired
    ArticleMapper articleMapper;

    @Autowired
    WorkMapper workMapper;

    @Autowired
    WorkFileMapper workFileMapper;

    @Autowired
    FileUtil fileUtil;

    /**
     * 管理员和大一同学头像上传
     */
    @Override
    public RestInfo imageUpload(String userId, MultipartFile file) throws RecruitFileException, ParameterError {
        log.info("用户userID为：" + userId + " 上传了头像");
        return fileService.imageUpload(userId, file);
    }

    /**
     * 文章文件上传-- markdown
     */
    @Override
    public RestInfo articleFileUpload(int articleId, MultipartFile file) throws RecruitFileException {
        log.info("文章编号为" + articleId + " 上传了附件");
        try {
            return fileService.articleFileUpload(articleId, file);
        } catch (RecruitFileException e) {
            //在标有@Transcation的方法上不能通过try-catch去维护数据一致性，因为一切都会回滚
            //保证一致性，文件添加失败，删除已经加上的文章记录
            articleMapper.deleteByArticleId(articleId);
            throw new RecruitFileException("文章文件上传失败");
        }
    }

    /**
     * 多个文章文件上传
     */
    @Override
    public RestInfo articleFilesUpload(int articleId, MultipartFile[] file) {
        log.info("文章编号为" + articleId + " 上传了多个附件");
        return fileService.articleFilesUpload(articleId, file);
    }

    /**
     * 发布作业的文件上传 回滚策略待定
     */
    @Override
    public RestInfo taskFileUpload(int taskId, MultipartFile file) throws RecruitFileException {
        log.info("任务编号为" + taskId + " 上传了文件");
        try {
            return fileService.taskFileUpload(taskId, file);
        } catch (RecruitFileException e) {
            //在标有@Transcation的方法上不能通过try-catch去维护数据库上的数据一致性
            //因为一切操作数据库语句都会回滚，但是操作到磁盘上的操作不会回滚(所以可能会有文件残留)
            //回滚（一致性）策略，若该任务没有对应的一个文件，就删除task表上的记录
            if (taskFileMapper.queryFileCount(taskId) == 0) {
                //taskMapper.deleteByTaskId(taskId);
                TaskPO taskPO = taskMapper.queryOneTaskById(taskId);
                if (taskPO != null) {
                    fileUtil.deleteDir(new File(fileUtil.buildTaskFilePath(taskPO.getCourseId(), taskId)));
                }
            }
            throw new RecruitFileException(e.getMessage());
        }
    }

    /**
     * 多个作业的文件上传
     */
    @Override
    public RestInfo taskFilesUpload(int taskId, MultipartFile[] files) {
        log.info("任务编号为" + taskId + " 上传了多个文件");
        int total = files.length;
        List<ErrorMsg> errorList = new ArrayList<>();
        for (int i = 0; i < total; i++) {
            try {
                this.taskFileUpload(taskId, files[i]);
            } catch (RecruitFileException e) {
                errorList.add(new ErrorMsg(405, "第" + (i + 1) + "个文件上传失败!", e.getMessage()));
            }
        }
        return RestInfo.success("要布置" + total + "个作业，" + "成功布置" + (total - errorList.size()) + "个作业文件", errorList);
    }

    /**
     * task文件的删除
     */
    @Override
    public RestInfo delTaskFile(int id, String adminId) throws RecruitException {
        log.info("管理员编号为：" + adminId + " 尝试删除任务编号为" + id + "的文件");
        return fileService.delTaskFile(id, adminId);
    }

    /**
     * 上交作业的文件上传
     */
    @Override
    public RestInfo workFileUpload(int workId, MultipartFile file) throws RecruitFileException {
        log.info("任务编号为" + workId + " 上传了文件");
        try {
            return fileService.workFileUpload(workId, file);
        } catch (RecruitFileException e) {
            //回滚（一致性）策略，若该作业记录没有对应的一个文件，就删除work表上的记录
            if (workFileMapper.queryFileCount(workId) == 0) {
                WorkPO workPO = workMapper.queryWorkById(workId);
                if (workPO != null) {
                    workMapper.deleteByWorkId(workId);
                    fileUtil.deleteDir(new File(fileUtil.buildWorkFilePath(workPO.getCourseId(), workPO.getUserId(), workPO.getTaskId())));
                }
            }
            throw new RecruitFileException(e.getMessage());
        }
    }

    /**
     * 多个作业的文件上传
     */
    @Override
    public RestInfo workFilesUpload(int workId, MultipartFile[] files) {
        log.info("任务编号为" + workId + " 上传了多个文件");
        int total = files.length;
        List<ErrorMsg> errorList = new ArrayList<>();
        for (int i = 0; i < total; i++) {
            try {
                //同一个类内调用@Transactional修饰的方法，事务不生效
                fileService.workFileUpload(workId, files[i]);
            } catch (RecruitFileException e) {
                errorList.add(new ErrorMsg(405, "第" + (i + 1) + "个作业文件上传失败!", e.getMessage()));
            }
        }
        //如果一个文件都没有上传成功就删除原work记录，清理磁盘上可能存在的未上传成功文件碎片
        if (workFileMapper.queryFileCount(workId) == 0) {
            WorkPO workPO = workMapper.queryWorkById(workId);
            if (workPO != null) {
                workMapper.deleteByWorkId(workId);
                fileUtil.deleteDir(new File(fileUtil.buildWorkFilePath(workPO.getCourseId(), workPO.getUserId(), workPO.getTaskId())));
            }
        }
        return RestInfo.success("成功提交" + (total - errorList.size()) + "个作业文件", errorList);
    }

    /**
     * work文件的删除（撤回上传的作业）
     */
    @Override
    public RestInfo delWorkFile(int id, String userId) throws RecruitException {
        log.info("用户编号为：" + userId + " 尝试删除已上传作业编号为" + id + "的文件");
        return fileService.delWorkFile(id, userId);
    }

    /**
     * announce文件上传  --断点续传（未做异常回滚操作）
     */
    @Override
    public RestInfo announceUpload(MultipartFile file, int shardIndex, int shardTotal, Integer fileSize, Integer courseId, String fileKey) throws RecruitFileException {
        //log.info("上传文件的第"+shardIndex+"片文件");
        return fileService.announceUpload(file, shardIndex, shardTotal, fileSize, courseId, fileKey);
    }

    /**
     * 检查数据库中有没有这个文件的存在(根据文件的唯一标识)
     */
    @Override
    public RestInfo check(String fileKey,int shardSize) {
        return fileService.check(fileKey,shardSize);
    }

    /**
     *合并announce分页文件
     */
    @Override
    public RestInfo merge(String fileKey,int id,String fileName) throws RecruitFileException, InterruptedException{
        return fileService.merge(fileKey,id,fileName);
    }
}
