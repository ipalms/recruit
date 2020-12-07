package com.geek.geekstudio.controller;

import com.geek.geekstudio.annotaion.AdminPermission;
import com.geek.geekstudio.annotaion.UserLoginToken;
import com.geek.geekstudio.model.vo.RestInfo;
import com.geek.geekstudio.service.AdminService;
import com.geek.geekstudio.service.proxy.AdminServiceProxy;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@Data
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    AdminServiceProxy adminServiceProxy;

    /**
     *报名人数（包括前端、后端等具体方向）
     */
    @AdminPermission
    @UserLoginToken
    @GetMapping("/countAllUser")
    public RestInfo countAllUser(@RequestParam(value = "courseId",required = false) Integer courseId){
        return adminServiceProxy.countAllUser(courseId);
    }
}
