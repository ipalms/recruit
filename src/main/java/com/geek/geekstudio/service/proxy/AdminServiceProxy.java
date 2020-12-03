package com.geek.geekstudio.service.proxy;

import com.geek.geekstudio.exception.NoUserException;
import com.geek.geekstudio.exception.ParameterError;
import com.geek.geekstudio.exception.UserRegisteredException;
import com.geek.geekstudio.model.po.AdminPO;
import com.geek.geekstudio.model.vo.RestInfo;
import com.geek.geekstudio.service.AdminService;
import com.geek.geekstudio.service.impl.AdminServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AdminServiceProxy implements AdminService {
    @Autowired
    AdminServiceImpl adminService;

    /**
     * 添加管理员
     */
    public RestInfo addAdmin(AdminPO adminPO) throws UserRegisteredException {
        log.info("添加管理员 adminPO —— adminId:"+adminPO.getAdminId()+" ,userName:"+adminPO.getAdminName()
                + ", password:"+adminPO.getPassword()+ ", courseName:"+adminPO.getCourseName());
        return adminService.addAdmin(adminPO);
    }

    /**
     * 删除管理员
     */
    @Override
    public RestInfo delAdmin(String userId) throws NoUserException {
        return adminService.delAdmin(userId);
    }

    @Override
    public RestInfo addCourse(int courseId, String courseName) throws ParameterError {
        log.info("添加课程：courseId--"+courseId+",courseName--"+courseName);
        return adminService.addCourse(courseId,courseName);
    }
}
