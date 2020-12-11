package com.geek.geekstudio.controller;

import com.geek.geekstudio.annotaion.AdminPermission;
import com.geek.geekstudio.annotaion.UserLoginToken;
import com.geek.geekstudio.model.dto.DailyMailDTO;
import com.geek.geekstudio.model.vo.RestInfo;
import com.geek.geekstudio.service.AdminService;
import com.geek.geekstudio.service.proxy.AdminServiceProxy;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    /**
     * 发送日常邮件功能 （包括向选择具体方向的大一新生  或具体的用户[userIdList]）
     */
    @AdminPermission
    @UserLoginToken
    @PostMapping("/sendDailyMail")
    public RestInfo sendDailyMail(@RequestBody DailyMailDTO dailyMailDTO){
        return adminServiceProxy.sendDailyMail(dailyMailDTO);
    }

}
