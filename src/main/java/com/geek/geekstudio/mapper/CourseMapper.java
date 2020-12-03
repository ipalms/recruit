package com.geek.geekstudio.mapper;


import com.geek.geekstudio.model.po.CoursePO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

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
     *添加课程
     */
    @Insert("INSERT INTO course (courseId,courseName,addTime) VALUES (#{courseId},#{courseName},#{addTime})")
    void addCourse(int courseId, String courseName, String addTime);
}
