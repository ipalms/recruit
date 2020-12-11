package com.geek.geekstudio.service.impl;

import com.geek.geekstudio.exception.NoUserException;
import com.geek.geekstudio.exception.ParameterError;
import com.geek.geekstudio.exception.RecruitException;
import com.geek.geekstudio.exception.RecruitFileException;
import com.geek.geekstudio.mapper.AdminMapper;
import com.geek.geekstudio.mapper.ArticleFileMapper;
import com.geek.geekstudio.mapper.SuperAdminMapper;
import com.geek.geekstudio.mapper.UserMapper;
import com.geek.geekstudio.model.po.AdminPO;
import com.geek.geekstudio.model.po.UserPO;
import com.geek.geekstudio.model.vo.ErrorMsg;
import com.geek.geekstudio.model.vo.RestInfo;
import com.geek.geekstudio.service.FileService;
import com.geek.geekstudio.util.DateUtil;
import com.geek.geekstudio.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class FileServiceImpl implements FileService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    SuperAdminMapper superAdminMapper;

    @Autowired
    AdminMapper adminMapper;

    @Autowired
    ArticleFileMapper articleFileMapper;

    @Autowired
    FileUtil fileUtil;

    /**
     * 管理员和大一同学头像上传 --覆盖掉之前的头像
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestInfo imageUpload(String userId, MultipartFile file) throws RecruitFileException, ParameterError {
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
    public RestInfo articleFileUpload(String articleId, MultipartFile file) throws RecruitFileException {
        String url=null;
        //同一文章上传同一名字文件会覆盖原文件
        String originalFileName = file.getOriginalFilename();
        String filePath = fileUtil.buildArticleFilePath(articleId);
        url = fileUtil.storeFile(filePath,file,originalFileName);
        //增加一条文件上传记录
        articleFileMapper.addUploadRecord(articleId,originalFileName,url,DateUtil.creatDate());
        return RestInfo.success("文章附件上传成功！",null);
    }

    /**
     *多个文章文件上传
     */
    @Override
    public RestInfo articleFilesUpload(String articleId, MultipartFile[] file) {
        int total=file.length;
        List<ErrorMsg> errorList = new ArrayList<>();
        for (int i = 0; i < total; i++) {
            try {
                articleFileUpload(articleId, file[i]);
            } catch (RecruitFileException e) {
                errorList.add(new ErrorMsg(405, "第" + (i + 1) + "个文件上传失败!", e.getMessage()));
            }
        }
        return RestInfo.success("成功上传"+(total-errorList.size())+"个文章附件",errorList);
    }
}
