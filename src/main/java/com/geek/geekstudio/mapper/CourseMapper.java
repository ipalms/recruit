package com.geek.geekstudio.mapper;


import com.geek.geekstudio.model.po.CoursePO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 定义对Course表的数据库操作
 */
@Repository
public interface CourseMapper {

    /**
     *通过课程号找课程
     */
    @Select("SELECT * FROM course WHERE courseId=#{courseId}")
    CoursePO queryCourseByCourseId(int courseId);

    /**
     *通过课程号找课程名
     */
    @Select("SELECT courseName FROM course WHERE courseId=#{courseId}")
    String queryCourseNameById(int courseId);

    /**
     *添加课程
     */
    @Insert("INSERT INTO course (courseId,courseName,addTime) VALUES (#{courseId},#{courseName},#{addTime})")
    void addCourse(int courseId, String courseName, String addTime);

    /**
     *删除课程
     */
    @Delete("DELETE FROM course WHERE courseId=#{courseId}")
    void deleteCourseByCourseId(int courseId);

    /**
     *查询所有的课程
     */
    @Select("SELECT * FROM course")
    List<CoursePO> queryCourse();
}
