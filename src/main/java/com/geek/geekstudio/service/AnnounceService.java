package com.geek.geekstudio.service;

import com.geek.geekstudio.exception.ParameterError;
import com.geek.geekstudio.exception.RecruitException;
import com.geek.geekstudio.model.dto.AnnounceDTO;
import com.geek.geekstudio.model.vo.RestInfo;
import org.springframework.web.multipart.MultipartFile;

public interface AnnounceService {

    //发布公告
    RestInfo addAnnounce(String adminId, Integer courseId, String title, String content, MultipartFile file)throws RecruitException;

    //删除公告
    RestInfo delAnnounce(int id)throws RecruitException;

    //查询公告
    RestInfo queryAnnounce(int page, int rows, int courseId);

    //查看一条公告的详情
    RestInfo queryOneAnnounce(int id) throws ParameterError;
}
