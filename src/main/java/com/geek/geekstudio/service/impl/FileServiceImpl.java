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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.io.*;

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

    @Autowired
    RedisTemplate<Object,Object> redisTemplate;

    /**
     * 管理员和大一同学头像上传 --覆盖掉之前的头像
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestInfo imageUpload(String userId, MultipartFile file) throws RecruitFileException, ParameterError {
        if(file.getSize()/1000>singleMaxImageSize){
            throw new RecruitFileException("头像大小超过10M，请重新上传");
        }
        UserPO userPO;
        AdminPO adminPO;
        String url;
        //删除之前的头像
        userPO=userMapper.queryUserByUserId(userId);
        if(userPO!=null){
            if(userPO.getImage()!=null&&userPO.getImage().contains("-"+userId)) {
                fileUtil.deleteFile(fileUtil.buildPath(userPO.getImage()));
            }
        }else {
            adminPO=superAdminMapper.queryAdminByAdminId(userId);
            if(adminPO.getImage()!=null&&adminPO.getImage().contains("-"+userId)){
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
            //同一文章上传同一名字文件会覆盖原文件
            String originalFileName = file.getOriginalFilename();
            String filePath = fileUtil.buildArticleFilePath(articleId);
            String url = fileUtil.storeFile(filePath,file,originalFileName);
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
            String url;
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
     * 解决并发上传导致插入重复的：
     *   1.使用synchronized锁住构造的字符串常量对象，对于判断到数据库无记录的情况先去尝试获取锁
     *       --缺点锁的效率问题，以及当请求次数变多会导致运行时常量池变大
     *   2.对fileKey加唯一索引，对于后续并发插入失败的情况catch异常然后执行update操作
     *     对于唯一索引坏处是每次插入的时候要全局扫描表中有没有该唯一索引的记录
     */
    @Override
    @Transactional
    public RestInfo announceUpload(MultipartFile file, int shardIndex,int shardSize, int shardTotal, Integer fileSize, Integer courseId, String fileKey) throws RecruitFileException {

        if((shardIndex<shardTotal&&file.getSize()<shardSize)||(shardIndex==shardTotal&&((shardTotal-1)*shardSize+file.getSize()!=fileSize))){
            throw new RecruitFileException("文件传输出错！");
        }
        //文件的名称(包含文件的扩展名)
        String filePath = fileUtil.buildAnnounceFilePath(courseId);
        //这个是分片的名字
        String fragmentFileName = fileKey + "." + shardIndex; // course\6sfSqfOwzmik4A4icMYuUe.mp4.1
        //保存这个分片到磁盘(同名文件覆盖)
        fileUtil.storeFile(filePath,file,fragmentFileName);
        //查询数据库中有无此文件的存在fileKey
        FragmentFilePO fragmentFilePO=announceMapper.queryFileByKey(fileKey);
        //数据库中的已上传分片大小小于当前的则进行替换
        if(fragmentFilePO!=null&&(fragmentFilePO.getShardIndex()<shardIndex)){
           announceMapper.updateFileInfo(fragmentFilePO.getId(),shardIndex,DateUtil.creatDate());
        }else if(fragmentFilePO==null) {
            //获取锁
            synchronized (fileKey.intern()){
                //再次判断记录有没有被插入进去
                fragmentFilePO=announceMapper.queryFileByKey(fileKey);
                if(fragmentFilePO==null){
                    filePath=fileUtil.buildAnnounceFullPath(filePath,fileKey);
                    announceMapper.insertFileInfo(filePath,fileSize,DateUtil.creatDate(),DateUtil.creatDate(),shardIndex,shardTotal,fileKey);
                }else {
                    //已经被其他线程抢先insert了
                    announceMapper.updateFileInfo(fragmentFilePO.getId(),shardIndex,DateUtil.creatDate());
                }
            }
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
            String path=fileUtil.buildPath(fragmentFilePO.getFilePath());
            int index=fragmentFilePO.getShardIndex();
            int total=fragmentFilePO.getShardTotal();
            int totalSize=fragmentFilePO.getFileSize();
            int needSend=index+1;
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
                    //fragmentFilePO.setShardIndex(i+1);
                    needSend=i+1;
                    break;
                }
            }
            if(index==total&&lastComplete){
                return RestInfo.success();
            }
            return RestInfo.success(needSend);
        }
        //返回状态码400，表示之前不存在该文件
        return RestInfo.failed();
    }

    /**
     *合并announce分页文件
     */
    @Override
    public RestInfo merge(String fileKey,int id,String fileName) throws RecruitFileException,InterruptedException {
        FragmentFilePO fragmentFilePO=announceMapper.queryFileByKey(fileKey);
        if(fragmentFilePO==null){
            RestInfo.failed("该文件不存在或已被修改，请重新上传");
        }
        //获取到的路径 没有.1 .2 这样的东西
        String mergeFilePath=fragmentFilePO.getFilePath().replaceAll(fileKey,fileName);
        String path=fileUtil.buildPath(fragmentFilePO.getFilePath());
        String realPath=fileUtil.buildPath(mergeFilePath);
        int shardTotal= fragmentFilePO.getShardTotal();
        FileOutputStream outputStream; // 文件追加写入
        try {
            outputStream = new FileOutputStream(realPath,true);
        } catch (FileNotFoundException e) {
            throw new RecruitFileException("文件合并出错");
        }
        FileInputStream fileInputStream = null; //分片文件
        byte[] byt = new byte[10 * 1024 * 1024];//一次写入10M
        int len;
        File part;
        try {
            for (int i = 0; i < shardTotal; i++) {
                part=new File(path + "." + (i + 1));
                if(!part.exists()){
                    throw new RecruitFileException("文件合并出错");
                }
                // 读取第i个分片
                fileInputStream = new FileInputStream(part); //  course\6sfSqfOwzmik4A4icMYuUe.mp4.1
                while ((len = fileInputStream.read(byt)) != -1) {
                    outputStream.write(byt, 0, len);
                    outputStream.flush();
                }
                //确保对每个文件的输入流都关闭才能删除掉该分片文件--因为这样java进程就没有占用该文件了
                fileInputStream.close();
            }
        } catch (IOException e) {
            throw new RecruitFileException("文件合并出错");
        } finally {
            try {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
                outputStream.flush();
                outputStream.close();
            } catch (Exception e) {
                log.error("IO流关闭出错", e);
            }
        }
        //回收垃圾
        //System.gc();
        //等待100毫秒 等待垃圾回收去 回收完垃圾
        //Thread.sleep(100);
        for (int i = 0; i < shardTotal; i++) {
            String filePath = path + "." + (i + 1);
            File file = new File(filePath);
            file.delete();
        }
        //同步数据库相关信息
        announceMapper.updateAnnounceFile(id,fileName,mergeFilePath);
        announceMapper.updateFilePath(fragmentFilePO.getId(),mergeFilePath,DateUtil.creatDate());
        AnnouncePO announcePO=announceMapper.findAnnounceById(id);
        AdminPO adminPO = superAdminMapper.queryByAdminId(announcePO.getAdminId());
        adminPO.setImage(fileUtil.getFileUrl(adminPO.getImage()));
        announcePO.setAdminPO(adminPO);
        //更新缓存
        redisTemplate.opsForHash().put("announce",id+"",announcePO);
        /*if(fragmentFilePO.getShardIndex()!=fragmentFilePO.getShardTotal()){
            announceMapper.updateFileInfo(fragmentFilePO.getId(),fragmentFilePO.getShardTotal(),DateUtil.creatDate());
        }*/
        return RestInfo.success();
    }
}
