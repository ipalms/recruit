package com.geek.geekstudio.service.proxy;

import com.geek.geekstudio.exception.ParameterError;
import com.geek.geekstudio.model.vo.RestInfo;
import com.geek.geekstudio.service.CourseService;
import com.geek.geekstudio.service.impl.CourseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CourseServiceProxy implements CourseService {
    @Autowired
    CourseServiceImpl courseService;

    /**
     *添加课程
     */
    @Override
    public RestInfo addCourse(int courseId, String courseName) throws ParameterError {
        log.info("添加课程：courseId--"+courseId+",courseName--"+courseName);
        RestInfo info=courseService.addCourse(courseId,courseName);
        //更新缓存中所有课程信息
        courseService.updateCourse();
        return info;
    }
    /**
     *删除课程
     */
    @Override
    public RestInfo delCourse(int courseId) throws ParameterError {
        log.info("删除课程：courseId--"+courseId);
        RestInfo info=courseService.delCourse(courseId);
        //更新缓存中所有课程信息
        courseService.updateCourse();
        return info;
    }

    /**
     * 查询所有开设课程
     */
    @Override
    public RestInfo queryCourse() {
        return courseService.queryCourse();
    }

    /**
     *查询自己的选择方向
     */
    @Override
    public RestInfo queryMyCourse(String userId) {
        return courseService.queryMyCourse(userId);
    }
}
