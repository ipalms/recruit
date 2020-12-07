package com.geek.geekstudio.service.impl;

import com.geek.geekstudio.mapper.AdminMapper;
import com.geek.geekstudio.model.vo.RestInfo;
import com.geek.geekstudio.service.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AdminServiceImpl implements AdminService {
    @Autowired
    AdminMapper adminMapper;

    /**
     *报名人数（包括前端、后端等具体方向）
     */
    @Override
    public RestInfo countAllUser(Integer courseId) {
        int count;
        if(courseId==null){
            count=adminMapper.countAllUser();
        }else {
            count = adminMapper.countDetailUser(courseId);
        }
        return RestInfo.success("成功获取到人数！",count);
    }
}
