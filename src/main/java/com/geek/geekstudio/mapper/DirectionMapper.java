package com.geek.geekstudio.mapper;

import com.geek.geekstudio.model.dto.DirectionDTO;
import com.geek.geekstudio.model.po.DirectionPO;
import com.geek.geekstudio.model.vo.DirectionVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 定义对direction表的数据库操作
 */
@Repository
public interface DirectionMapper {

    //查询选择记录
    @Select("SELECT * FROM direction WHERE userId=#{userId} AND courseId=#{courseId}")
    DirectionPO queryByUserIdAndCourseId(String userId, int courseId);

    //大一同学选择方向
    @Insert("INSERT INTO direction (userId,courseId,addTime) VALUES (#{userId},#{courseId},#{addTime})")
    void addDirection(DirectionDTO directionDTO);

    //大一同学撤销已选择方向
    @Delete("DELETE FROM direction WHERE userId=#{userId} AND courseId=#{courseId} ")
    void delByUserIdAndCourseId(String userId, int courseId);

    //查询自己的选择方向
    @Select("SELECT userId,courseId,addTime FROM direction WHERE userId=#{userId}")
    List<DirectionVO> queryMyCourse(String userId);
}
