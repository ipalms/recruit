package com.geek.geekstudio.controller;

import com.geek.geekstudio.annotaion.PassToken;
import com.geek.geekstudio.annotaion.UserLoginToken;
import com.geek.geekstudio.model.vo.RestInfo;
import com.geek.geekstudio.service.proxy.CourseServiceProxy;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
@Data
@RequestMapping("/course")
public class CourseController {
    @Autowired
    CourseServiceProxy courseServiceProxy;

    /**
     * 查询所有开设课程
     */
    @PassToken
    @GetMapping("/queryCourse")
    public RestInfo queryCourse(){
        return courseServiceProxy.queryCourse();
    }

    /**
     *查询自己所选方向（大一同学）
     */
    @UserLoginToken
    @GetMapping("/queryMyCourse")
    public RestInfo queryMyCourse(@RequestParam("userId") String userId){
        return courseServiceProxy.queryMyCourse(userId);
    }
}
