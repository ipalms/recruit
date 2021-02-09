package com.geek.geekstudio.service.proxy;

import com.geek.geekstudio.exception.ParameterError;
import com.geek.geekstudio.exception.RecruitException;
import com.geek.geekstudio.exception.RecruitFileException;
import com.geek.geekstudio.mapper.ArticleMapper;
import com.geek.geekstudio.mapper.TaskFileMapper;
import com.geek.geekstudio.mapper.TaskMapper;
import com.geek.geekstudio.model.vo.RestInfo;
import com.geek.geekstudio.service.FileService;
import com.geek.geekstudio.service.impl.FileServiceImpl;
import com.geek.geekstudio.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

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
    FileUtil fileUtil;

    /**
     * 管理员和大一同学头像上传
     */
    @Override
    public RestInfo imageUpload(String userId, MultipartFile file) throws RecruitFileException, ParameterError {
        log.info("用户userID为："+userId+" 上传了头像");
        return fileService.imageUpload(userId,file);
    }

    /**
     *文章文件上传-- markdown
     */
    @Override
    public RestInfo articleFileUpload(int articleId, MultipartFile file) throws RecruitFileException {
        log.info("文章编号为"+articleId+" 上传了附件");
        try {
            return fileService.articleFileUpload(articleId,file);
        } catch (RecruitFileException e) {
            //在标有@Transcation的方法上不能通过try-catch去维护数据一致性，因为一切都会回滚
            //保证一致性，文件添加失败，删除已经加上的文章记录
            articleMapper.deleteByArticleId(articleId);
            throw new RecruitFileException("文章文件上传失败");
        }
    }

    /**
     *多个文章文件上传
     */
    @Override
    public RestInfo articleFilesUpload(int articleId, MultipartFile[] file) {
        log.info("文章编号为"+articleId+" 上传了多个附件");
        return fileService.articleFilesUpload(articleId,file);
    }

    /**
     *发布作业的文件上传
     */
    @Override
    public RestInfo taskFileUpload(int taskId, MultipartFile file) throws RecruitFileException {
        log.info("任务编号为"+taskId+" 上传了文件");
        try {
            return fileService.taskFileUpload(taskId,file);
        } catch (RecruitFileException e) {
            //在标有@Transcation的方法上不能通过try-catch去维护数据一致性
            //因为一切操作数据库语句都会回滚，但是操作到磁盘上的操作不会回滚(所以可能会有文件残留)
            //回滚（一致性）策略，若该任务没有对应的一个文件，就删除task表上的记录
            if(taskFileMapper.queryFileCount(taskId)==0){
                taskMapper.deleteByTaskId(taskId);
                fileUtil.deleteDir(new File(fileUtil.buildTaskFilePath(taskId)));
            }
            throw new RecruitFileException("task文件上传失败");
        }
    }

    /**
     *多个作业的文件上传
     */
    @Override
    public RestInfo taskFilesUpload(int taskId, MultipartFile[] files) {
        log.info("任务编号为"+taskId+" 上传了多个文件");
        return fileService.taskFilesUpload(taskId,files);
    }
}
