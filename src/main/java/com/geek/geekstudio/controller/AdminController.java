package com.geek.geekstudio.controller;

import com.geek.geekstudio.annotaion.SuperAdminPermission;
import com.geek.geekstudio.annotaion.UserLoginToken;
import com.geek.geekstudio.exception.NoUserException;
import com.geek.geekstudio.exception.ParameterError;
import com.geek.geekstudio.exception.UserRegisteredException;
import com.geek.geekstudio.model.po.AdminPO;
import com.geek.geekstudio.model.vo.RestInfo;
import com.geek.geekstudio.service.AdminService;
import com.geek.geekstudio.service.proxy.AdminServiceProxy;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@RestController
@Validated
@Data
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    AdminServiceProxy adminServiceProxy;

    /**
     * 添加管理员
     */
    @UserLoginToken
    @SuperAdminPermission
    @PostMapping("/addAdmin")
    public RestInfo addAdmin(@Valid AdminPO adminPO) throws UserRegisteredException {
        return adminServiceProxy.addAdmin(adminPO);
    }

    /**
     * 删除管理员
     */
    @UserLoginToken
    @SuperAdminPermission
    @PostMapping("/delAdmin")
    public RestInfo delAdmin(@NotBlank(message ="用户ID不能为空") @Length(min = 5,max = 30) String userId) throws NoUserException {
        return adminServiceProxy.delAdmin(userId);
    }

    /**
     * 添加课程
     */
    @UserLoginToken
    @SuperAdminPermission
    @PostMapping("/addCourse")
    public RestInfo addCourse(@NotNull(message = "课程ID不为空")  int courseId, @NotBlank(message = "课程名不能为空")String courseName) throws ParameterError {
        return adminServiceProxy.addCourse(courseId,courseName);
    }
}
