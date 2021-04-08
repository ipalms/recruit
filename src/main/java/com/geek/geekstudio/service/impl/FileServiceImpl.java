package com.geek.geekstudio.service.impl;

import com.geek.geekstudio.exception.*;
import com.geek.geekstudio.mapper.*;
import com.geek.geekstudio.model.po.*;
import com.geek.geekstudio.model.vo.ErrorMsg;
import com.geek.geekstudio.model.vo.RestInfo;
import com.geek.geekstudio.model.vo.TaskVO;
import com.geek.geekstudio.model.vo.WorkVO;
import com.geek.geekstudio.service.FileService;
import com.geek.geekstudio.util.DateUtil;
import com.geek.geekstudio.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class FileServiceImpl implements FileService {

    //单个文件最大容量100M
    private static long singleMaxFileSize=100*1024;

    //头像最大10M
    private static long singleMaxImageSize=10*1024;

    //单个作业文件最大15M
    private static long singleMaxWorkSize=15*1024;

    @Autowired
    UserMapper userMapper;

    @Autowired
    SuperAdminMapper superAdminMapper;

    @Autowired
    AdminMapper adminMapper;

    @Autowired
    ArticleMapper articleMapper;

    @Autowired
    ArticleFileMapper articleFileMapper;

    @Autowired
    TaskMapper taskMapper;

    @Autowired
    TaskFileMapper taskFileMapper;

    @Autowired
    WorkMapper workMapper;

    @Autowired
    WorkFileMapper workFileMapper;

    @Autowired
    AnnounceMapper announceMapper;

    @Autowired
    FileUtil fileUtil;

    /**
     * 管理员和大一同学头像上传 --覆盖掉之前的头像
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestInfo imageUpload(String userId, MultipartFile file) throws RecruitFileException, ParameterError {
        if(file.getSize()/1000>singleMaxImageSize){
            throw new RecruitFileException("头像大小超过10M，请重新上传");
        }
        UserPO userPO=null;
        AdminPO adminPO=null;
        String url = null;
        //删除之前的头像
        userPO=userMapper.queryUserByUserId(userId);
        if(userPO!=null){
            if(userPO.getImage()!=null) {
                fileUtil.deleteFile(fileUtil.buildPath(userPO.getImage()));
            }
        }else {
            adminPO=superAdminMapper.queryAdminByAdminId(userId);
            if(adminPO.getImage()!=null){
                fileUtil.deleteFile(fileUtil.buildPath(adminPO.getImage()));
            }
        }
        String originalFileName = file.getOriginalFilename();
        int index = originalFileName.indexOf('.');
        //确保头像名称唯一
        String fileName = originalFileName.substring(0,index) + "-" + userId + originalFileName.substring(index);
        String filePath = fileUtil.getImageFilePath();
        url = fileUtil.storeFile(filePath,file,fileName);
        //更新头像的的url地址
        if(userPO!=null){
            userMapper.updateUserImage(userId,url);
        }else {
            adminMapper.updateAdminImage(userId,url);
        }
        return RestInfo.success("头像上传成功！",null);
    }

    /**
     *文章文件上传-- markdown
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestInfo articleFileUpload(int articleId, MultipartFile file) throws RecruitFileException {
        try {
            if(file.getSize()/1000>singleMaxImageSize){
                throw new RecruitFileException("文件大于10M，请重新上传");
            }
            String url=null;
            //同一文章上传同一名字文件会覆盖原文件
            String originalFileName = file.getOriginalFilename();
            String filePath = fileUtil.buildArticleFilePath(articleId);
            url = fileUtil.storeFile(filePath,file,originalFileName);
            //增加一条文件上传记录
            ArticleFilePO articleFilePO=articleFileMapper.queryArticleFileByFilePath(url);
            if(articleFilePO==null) {
                articleFileMapper.addUploadRecord(articleId,originalFileName,url,DateUtil.creatDate());
            }else {
                articleFileMapper.updateRecordTime(articleFilePO.getId(),DateUtil.creatDate());
            }
            return RestInfo.success("文章附件上传成功！",null);
        } catch (Exception e) {
            throw new RecruitFileException();
        }
    }

    /**
     *多个文章文件上传
     */
    @Override
    //@Transactional(rollbackFor = Exception.class)
    public RestInfo articleFilesUpload(int articleId, MultipartFile[] file) {
        int total=file.length;
        for (int i = 0; i < total; i++) {
            try {
                //同一个类内调用@Transactional修饰的方法，事务不生效
                articleFileUpload(articleId, file[i]);
            } catch (RecruitFileException e) {
                //捕获到一个异常就中止文件的上传
                articleFileMapper.deleteFilesByArticleId(articleId);
                articleMapper.deleteByArticleId(articleId);
                return RestInfo.failed(405,"部分文件上传失败",new ErrorMsg(405, "第" + (i + 1) + "个文件上传失败!", e.getMessage()));
            }
        }
        return RestInfo.success("成功上传"+total+"个文章附件",null);
    }

    /**
     *发布作业的文件上传
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestInfo taskFileUpload(int taskId, MultipartFile file) throws RecruitFileException {
        try {
            if(file.getSize()/1000>singleMaxFileSize){
                throw new RecruitFileException("单个文件大于100M，请重新上传");
            }
            TaskPO taskPO=taskMapper.queryOneTaskById(taskId);
            if(taskPO==null){
                throw new ParameterError();
            }
            String url=null;
            //上传同一名字文件会覆盖原文件
            String originalFileName = file.getOriginalFilename();
            String filePath = fileUtil.buildTaskFilePath(taskPO.getCourseId(),taskId);
            url = fileUtil.storeFile(filePath,file,originalFileName);
            //int i=1/0; //测试异常
            //对上传同名文件上传的处理
            TaskFilePO taskFilePO=taskFileMapper.queryTaskFileByFilePath(url);
            if(taskFilePO==null){
                taskFileMapper.addTaskRecord(taskId,originalFileName,url,DateUtil.creatDate());
            }else {
                taskFileMapper.updateRecordTime(taskFilePO.getId(),DateUtil.creatDate());
            }
            return RestInfo.success("上传作业文件成功！",null);
        } catch (Exception e) {
            throw new RecruitFileException(e.getMessage());
        }
    }

    //空实现
    @Override
    public RestInfo taskFilesUpload(int taskId, MultipartFile[] files) {
        return null;
    }

    /*    *//**
     *多个作业的文件上传--没有事务支持，除非把此方法内容放至到proxy类调用
     *//*
    @Override
    public RestInfo taskFilesUpload(int taskId, MultipartFile[] files) {
        int total=files.length;
        List<ErrorMsg> errorList = new ArrayList<>();
        for (int i = 0; i < total; i++) {
            try {
                //同一个类内调用@Transactional修饰的方法，事务不生效
                taskFileUpload(taskId, files[i]);
            } catch (RecruitFileException e) {
                errorList.add(new ErrorMsg(405, "第" + (i + 1) + "个文件上传失败!", e.getMessage()));
            }
        }
        return RestInfo.success("成功布置"+(total-errorList.size())+"个作业文件",errorList);
    }*/

    /**
     * task文件的删除（目前仅删除taskfile相应的记录和磁盘中的文件）
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestInfo delTaskFile(int id, String adminId) throws RecruitException {
        TaskVO taskVO =taskFileMapper.queryTaskById(id);
        if(taskVO==null){
            throw new ParameterError();
        }
        if(!(taskVO.getAdminId().equals(adminId))&&!("super".equals(adminMapper.queryTypeById(adminId)))){
            throw new PermissionDeniedException("非发布该作业管理员，不可删除！");
        }
        taskFileMapper.deleteFileById(id);
        fileUtil.deleteFile(fileUtil.buildPath(taskVO.getFilePath()));
        return RestInfo.success("删除作业文件成功！",null);
    }

    /**
     *上交作业的文件上传
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestInfo workFileUpload(int workId, MultipartFile file) throws RecruitFileException{
        try {
            if(file.getSize()/1000>singleMaxWorkSize){
                throw new RecruitFileException("单个文件大于15M，请重新上传");
            }
            WorkPO workPO=workMapper.queryWorkById(workId);
            if(workPO==null){
                throw new ParameterError();
            }
            String url;
            //上传同一名字文件会覆盖原文件
            String originalFileName = file.getOriginalFilename();
            String filePath = fileUtil.buildWorkFilePath(workPO.getCourseId(),workPO.getUserId(),workPO.getTaskId());
            url = fileUtil.storeFile(filePath,file,originalFileName);
            //对上传同名文件的处理
            WorkFilePO workFilePO=workFileMapper.queryWorkFileByFilePath(url);
            if(workFilePO==null){
                workFileMapper.addWorkRecord(workId,originalFileName,url,DateUtil.creatDate());
            }else {
                workFileMapper.updateRecordTime(workFilePO.getId(),DateUtil.creatDate());
            }
            return RestInfo.success("提交作业成功！",null);
        } catch (Exception e) {
            throw new RecruitFileException(e.getMessage());
        }
    }

    //空实现
    @Override
    public RestInfo workFilesUpload(int workId, MultipartFile[] files) {
        return null;
    }

    /**
     *多个作业的文件上传
     *//*
    @Override
    public RestInfo workFilesUpload(int workId, MultipartFile[] files) {
        int total=files.length;
        List<ErrorMsg> errorList = new ArrayList<>();
        for (int i = 0; i < total; i++) {
            try {
                //同一个类内调用@Transactional修饰的方法，事务不生效
                workFileUpload(workId, files[i]);
            } catch (RecruitFileException e) {
                errorList.add(new ErrorMsg(405, "第" + (i + 1) + "个作业文件上传失败!", e.getMessage()));
            }
        }
        return RestInfo.success("成功上传"+(total-errorList.size())+"个作业文件",errorList);
    }*/

    /**
     * work文件的删除（撤回上传的作业）
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestInfo delWorkFile(int id, String userId) throws RecruitException {
        WorkVO workVO =workFileMapper.queryWorkById(id);
        if(workVO==null){
            throw new ParameterError();
        }
        if(!(workVO.getUserId().equals(userId))){
            throw new PermissionDeniedException("当前操作者非作业上传者，不可删除！");
        }
        workFileMapper.deleteFileById(id);
        //如果删除到没有一个文件记录，就把work表中的记录也删除
        if (workFileMapper.queryFileCount(workVO.getId()) == 0) {
            workMapper.deleteByWorkId(workVO.getId());
            //fileUtil.deleteDir(new File(fileUtil.buildWorkFilePath(workVO.getCourseId(),workVO.getUserId(),workVO.getTaskId())));
        }
        fileUtil.deleteFile(fileUtil.buildPath(workVO.getFilePath()));
        return RestInfo.success("删除上传作业文件成功！",null);
    }

    /**
     * announce文件上传  --断点续传
     */
    @Override
    public RestInfo announceUpload(MultipartFile file, int shardIndex, int shardTotal, Integer fileSize, String fileName,  Integer courseId, String fileKey) throws RecruitFileException {
        //文件的名称(包含文件的扩展名)
        String filePath = fileUtil.buildAnnounceFilePath(courseId);
        //这个是分片的名字
        String fragmentFileName = fileName + "." + shardIndex; // course\6sfSqfOwzmik4A4icMYuUe.mp4.1
        //保存这个分片到磁盘(同名文件覆盖)
        fileUtil.storeFile(filePath,file,fragmentFileName);
        //查询数据库中有无此文件的存在
        FragmentFilePO fragmentFilePO=announceMapper.queryFileByKey(fileKey);
        //数据库中的已上传分片大小小于当前的则进行替换
        if(fragmentFilePO!=null&&(fragmentFilePO.getShardIndex()<shardIndex)){
           announceMapper.updateFileInfo(fragmentFilePO.getId(),shardIndex,DateUtil.creatDate());
        }else if(fragmentFilePO==null) {
            filePath=fileUtil.buildAnnounceFullPath(filePath,fileName);
            announceMapper.insertFileInfo(fileName,filePath,fileSize,DateUtil.creatDate(),DateUtil.creatDate(),shardIndex,shardTotal,fileKey);
        }
        return RestInfo.success();
    }

    /**
     * 检查数据库中有没有这个文件的存在(根据文件的唯一标识)
     */
    @Override
    public RestInfo check(String fileKey,int shardSize) {
        FragmentFilePO fragmentFilePO=announceMapper.queryFileByKey(fileKey);
        //说明这个文件之前上传过(找到文件中最稳定的断开片段,即该片段前的每一片段都存在)
        if(fragmentFilePO!=null){
            log.info("中断过的文件check");
            String path=fileUtil.buildPath(fragmentFilePO.getFilePath());
            int index=fragmentFilePO.getShardIndex();
            int total=fragmentFilePO.getShardTotal();
            int totalSize=fragmentFilePO.getFileSize();
            boolean lastComplete =true;
            for (int i = 0; i <index ; i++) {
                File part1=new File(path + "." + (i + 1));
                //如果index对应文件及之前的文件不存在，或大小小于规定大小（文件缺失了），
                //或最后一片不完整就设置新的index结点
                if((part1.exists()&&part1.length()<shardSize)||!part1.exists()||(i+1==total&&((total-1)*shardSize+part1.length()!=totalSize))){
                    //往后的文件可以不删除，因为同名的文件会覆盖
                    /*for(int j=i; j<total;j++){
                        File part2=new File(path + "." + (j + 1));
                        if(part2.exists()){
                            //删除文件不再做进一步判断
                            part2.delete();
                        }
                    }*/
                    announceMapper.updateFileInfo(fragmentFilePO.getId(),i,DateUtil.creatDate());
                    if(i+1==total){
                        lastComplete=false;
                    }
                    //更改index值为i+1
                    fragmentFilePO.setShardIndex(i+1);
                    break;
                }
            }
            if(index==total&&lastComplete){
                return RestInfo.success();
            }
            return RestInfo.success(fragmentFilePO);
        }
        //返回状态码400，表示之前不存在该文件
        return RestInfo.failed();
    }

    /**
     *合并announce分页文件
     */
    @Override
    public RestInfo merge(String fileKey,int id) throws RecruitFileException,InterruptedException {
        FragmentFilePO fragmentFilePO=announceMapper.queryFileByKey(fileKey);
        if(fragmentFilePO==null){
            RestInfo.failed("该文件不存在或已被修改，请重新上传");
        }
        /*//在网络请求中不能确定最后一个分片是否等于total index 为integer型
        if(fragmentFilePO.getShardIndex()<fragmentFilePO.getShardTotal()){
            RestInfo.failed("该文件尚未上传完毕，不可合并");
        }*/
        //合并分片开始
        log.info("分片合并开始");
        //获取到的路径 没有.1 .2 这样的东西
        String path=fileUtil.buildPath(fragmentFilePO.getFilePath());
        int shardTotal= fragmentFilePO.getShardTotal();
        FileOutputStream outputStream = null; // 文件追加写入
        try {
            outputStream = new FileOutputStream(path,true);
        } catch (FileNotFoundException e) {
            throw new RecruitFileException("文件合并出错");
        }
        FileInputStream fileInputStream = null; //分片文件
        byte[] byt = new byte[10 * 1024 * 1024];//一次写入10M
        int len;
        try {
            for (int i = 0; i < shardTotal; i++) {
                File part=new File(path + "." + (i + 1));
                if(!part.exists()){
                    throw new RecruitFileException("文件合并出错");
                }
                // 读取第i个分片
                fileInputStream = new FileInputStream(part); //  course\6sfSqfOwzmik4A4icMYuUe.mp4.1
                while ((len = fileInputStream.read(byt)) != -1) {
                    outputStream.write(byt, 0, len);
                }
            }
        } catch (IOException e) {
            throw new RecruitFileException("文件合并出错");
        } finally {
            try {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
                outputStream.close();
            } catch (Exception e) {
                log.error("IO流关闭出错", e);
            }
        }
        log.info("分片结束了");
        //告诉java虚拟机去回收垃圾 至于什么时候回收  这个取决于 虚拟机的决定
        System.gc();
        //等待100毫秒 等待垃圾回收去 回收完垃圾
        Thread.sleep(100);
        log.info("删除分片开始");
        for (int i = 0; i < shardTotal; i++) {
            String filePath = path + "." + (i + 1);
            File file = new File(filePath);
            file.delete();
        }
        //同步数据库相关信息
        announceMapper.updateAnnounceFile(id,fragmentFilePO.getFileName(),fragmentFilePO.getFilePath());
        if(fragmentFilePO.getShardIndex()!=fragmentFilePO.getShardTotal()){
            announceMapper.updateFileInfo(fragmentFilePO.getId(),fragmentFilePO.getShardTotal(),DateUtil.creatDate());
        }
        return RestInfo.success();
    }
}
