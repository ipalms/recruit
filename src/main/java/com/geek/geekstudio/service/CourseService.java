package com.geek.geekstudio.service;


import com.geek.geekstudio.exception.ParameterError;
import com.geek.geekstudio.model.vo.RestInfo;

public interface CourseService {

    //添加课程
    RestInfo addCourse(int courseId, String courseName) throws ParameterError;

    //删除课程
    RestInfo delCourse(int courseId) throws ParameterError;

    //查询课程
    RestInfo queryCourse();

    //查询自己所选方向
    RestInfo queryMyCourse(String userId);
}
