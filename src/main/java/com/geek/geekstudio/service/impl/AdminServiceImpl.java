package com.geek.geekstudio.service.impl;

import com.geek.geekstudio.exception.NoUserException;
import com.geek.geekstudio.exception.ParameterError;
import com.geek.geekstudio.exception.UserRegisteredException;
import com.geek.geekstudio.mapper.AdminMapper;
import com.geek.geekstudio.mapper.CourseMapper;
import com.geek.geekstudio.model.po.AdminPO;
import com.geek.geekstudio.model.po.CoursePO;
import com.geek.geekstudio.model.vo.RestInfo;
import com.geek.geekstudio.service.AdminService;
import com.geek.geekstudio.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class AdminServiceImpl implements AdminService {

    @Autowired
    AdminMapper adminMapper;
    @Autowired
    CourseMapper courseMapper;

    /**
     * 添加管理员
     */
    @Override
    public RestInfo addAdmin(AdminPO adminPO) throws UserRegisteredException {
        if(adminMapper.queryAdminByAdminId(adminPO.getAdminId())!=null){
            throw new UserRegisteredException("此管理员ID已被注册");
        }
        adminPO.setRegisterTime(DateUtil.creatDate());
        adminPO.setType("admin");
        adminMapper.addAdmin(adminPO);
        return RestInfo.success("管理员添加成功！",null);
    }

    /**
     * 删除管理员
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public RestInfo delAdmin(String userId) throws NoUserException {
        AdminPO adminPO=adminMapper.queryAdminByAdminId(userId);
        if(adminPO==null){
            throw new NoUserException("无此用户");
        }
        adminMapper.deleteAdminByAdminId(userId);
        log.info("删除管理员：adminId--"+userId);
        return RestInfo.success("管理员删除成功！",null);
    }

    @Override
    public RestInfo addCourse(int courseId, String courseName) throws ParameterError {
        CoursePO coursePO=courseMapper.queryCourseByCourseId(courseId);
        if(coursePO!=null){
            throw new ParameterError("课程号已存在，不能再添加");
        }
        String addTime=DateUtil.creatDate();
        courseMapper.addCourse(courseId,courseName,addTime);
        return RestInfo.success("添加课程成功！",null);
    }

}
