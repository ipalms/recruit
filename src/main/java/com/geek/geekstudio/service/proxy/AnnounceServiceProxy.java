package com.geek.geekstudio.service.proxy;

import com.geek.geekstudio.exception.ParameterError;
import com.geek.geekstudio.exception.RecruitException;
import com.geek.geekstudio.model.vo.RestInfo;
import com.geek.geekstudio.service.AnnounceService;
import com.geek.geekstudio.service.impl.AnnounceServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
public class AnnounceServiceProxy implements AnnounceService {
    @Autowired
    AnnounceServiceImpl announceServiceImpl;

    /**
     *发布公告
     */
    @Override
    public RestInfo addAnnounce(String adminId, Integer courseId, String title, String content, MultipartFile file) throws RecruitException {
        log.info("管理员："+adminId+"发布了公告");
        return announceServiceImpl.addAnnounce(adminId,courseId,title,content,file);
    }

    /**
     * 删除公告
     */
    @Override
    public RestInfo delAnnounce(int id) throws RecruitException {
        log.info("删除id为"+" 的公告");
        return announceServiceImpl.delAnnounce(id);
    }

    /**
     * 查看发布的公告
     */
    @Override
    public RestInfo queryAnnounce(int page, int rows, int courseId) {
        return announceServiceImpl.queryAnnounce(page,rows,courseId);
    }

    /**
     * 查看发布的公告
     */
    @Override
    public RestInfo queryOneAnnounce(int id) throws ParameterError {
        return announceServiceImpl.queryOneAnnounce(id);
    }
}
