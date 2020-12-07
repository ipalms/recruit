package com.geek.geekstudio.service.proxy;

import com.geek.geekstudio.exception.NoUserException;
import com.geek.geekstudio.exception.PermissionDeniedException;
import com.geek.geekstudio.exception.UserRegisteredException;
import com.geek.geekstudio.model.po.AdminPO;
import com.geek.geekstudio.model.vo.RestInfo;
import com.geek.geekstudio.service.SuperAdminService;
import com.geek.geekstudio.service.impl.SuperAdminServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class SuperAdminServiceProxy implements SuperAdminService {
    @Autowired
    SuperAdminServiceImpl superAdminService;

    /**
     * 添加管理员
     */
    public RestInfo addAdmin(AdminPO adminPO) throws UserRegisteredException {
        log.info("添加管理员 adminPO —— adminId:"+adminPO.getAdminId()+" ,userName:"+adminPO.getAdminName()
                + ", password:"+adminPO.getPassword()+ ", courseName:"+adminPO.getCourseName());
        return superAdminService.addAdmin(adminPO);
    }

    /**
     * 删除管理员
     */
    @Override
    public RestInfo delAdmin(String userId) throws NoUserException {
        log.info("删除管理员 adminId="+userId);
        return superAdminService.delAdmin(userId);
    }

    @Override
    public RestInfo delAdmins(List<Object> userIdList) {
        return superAdminService.delAdmins(userIdList);
    }

    /**
     * 更新一个管理员信息
     */
    @Override
    public RestInfo updateAdmin(AdminPO adminPO) throws UserRegisteredException, PermissionDeniedException {
        log.info("更新管理员 adminId="+adminPO.getId()+"，password="+adminPO.getPassword());
        return superAdminService.updateAdmin(adminPO);
    }

    /**
     * 查询管理员
     */
    @Override
    public RestInfo queryAdmins(int page, int rows, String courseName, String adminName, String adminId) {
        return superAdminService.queryAdmins(page,rows,courseName,adminName,adminId);
    }

}
