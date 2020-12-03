package com.geek.geekstudio.service;

import com.geek.geekstudio.exception.NoUserException;
import com.geek.geekstudio.exception.ParameterError;
import com.geek.geekstudio.exception.UserRegisteredException;
import com.geek.geekstudio.model.po.AdminPO;
import com.geek.geekstudio.model.vo.RestInfo;

public interface AdminService {

    //添加管理员
    RestInfo addAdmin(AdminPO adminPO) throws UserRegisteredException;

    //删除管理员
    RestInfo delAdmin(String userId) throws NoUserException;

    //添加课程
    RestInfo addCourse(int courseId, String courseName) throws ParameterError;
}
