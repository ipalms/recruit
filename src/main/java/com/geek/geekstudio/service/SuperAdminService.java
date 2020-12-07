package com.geek.geekstudio.service;

import com.geek.geekstudio.exception.NoUserException;
import com.geek.geekstudio.exception.ParameterError;
import com.geek.geekstudio.exception.PermissionDeniedException;
import com.geek.geekstudio.exception.UserRegisteredException;
import com.geek.geekstudio.model.po.AdminPO;
import com.geek.geekstudio.model.vo.RestInfo;

import java.util.List;

public interface SuperAdminService {

    //添加管理员
    RestInfo addAdmin(AdminPO adminPO) throws UserRegisteredException;

    //删除管理员
    RestInfo delAdmin(String userId) throws NoUserException;

    //删除多个管理员
    RestInfo delAdmins(List<Object> userIdList);

    //更新一个管理员信息
    RestInfo updateAdmin(AdminPO adminPO) throws UserRegisteredException, PermissionDeniedException;

    //查询管理员--可根据传入的条件
    RestInfo queryAdmins(int page, int rows, String courseName, String adminName, String adminId);
}
