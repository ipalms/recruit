package com.geek.geekstudio.service.proxy;

import com.geek.geekstudio.exception.ParameterError;
import com.geek.geekstudio.exception.RecruitException;
import com.geek.geekstudio.exception.RecruitFileException;
import com.geek.geekstudio.model.vo.RestInfo;
import com.geek.geekstudio.service.FileService;
import com.geek.geekstudio.service.impl.FileServiceImpl;
import com.geek.geekstudio.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
public class FileServiceProxy implements FileService {

    @Autowired
    FileServiceImpl fileService;

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
    public RestInfo articleFileUpload(String articleId, MultipartFile file) throws RecruitFileException {
        log.info("文章编号为"+articleId+" 上传了附件");
        return fileService.articleFileUpload(articleId,file);
    }

    /**
     *多个文章文件上传
     */
    @Override
    public RestInfo articleFilesUpload(String articleId, MultipartFile[] file) {
        return fileService.articleFilesUpload(articleId,file);
    }
}
