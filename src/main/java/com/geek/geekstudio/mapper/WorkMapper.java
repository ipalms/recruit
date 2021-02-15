package com.geek.geekstudio.mapper;

import com.geek.geekstudio.model.dto.WorkDTO;
import com.geek.geekstudio.model.po.WorkPO;
import com.geek.geekstudio.model.vo.WorkVO;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkMapper {

    //增加学生提交作业记录，返回自增主键
    @Insert("INSERT INTO work (taskId,courseId,userId,addTime) " +
            "VALUES (#{taskId},#{courseId},#{userId},#{addTime})")
    @Options(useGeneratedKeys = true,keyColumn = "id",keyProperty = "id")
    int addWork(WorkDTO workDTO);

    //查询一条work记录
    @Select("SELECT * FROM work WHERE id=#{id}")
    WorkPO queryWorkById(int id);

    //删除一条work记录
    @Delete("DELETE FROM work WHERE id=#{id}")
    void deleteByWorkId(int id);

    //查询发布work的 userId,courseId,taskId
    @Select("SELECT id,courseId,userId,taskId FROM work WHERE id=#{id}")
    WorkDTO queryUserIdById(int id);

    //查询自己提交的所有记录
    @Select("SELECT * FROM work WHERE userId=#{userId} AND courseId=#{courseId}")
    List<WorkVO> queryWorkByCidAndUid(int courseId, String userId);

    //查询一条work记录
    @Select("SELECT * FROM work WHERE taskId=#{taskId} AND userId=#{userId}")
    WorkVO queryWorkByIDS(int taskId, String userId);

    //查询某个任务提交作业的总数
    @Select("SELECT COUNT(*) FROM work WHERE taskId=#{taskId}")
    int querySubmitTotal(int taskId);

    //分页查询大一同学提交的作业
    @Select("SELECT * FROM work WHERE taskId=#{taskId} LIMIT #{start},#{rows}")
    List<WorkVO> queryWorksByTaskId(int taskId, int start, int rows);

    //给某项作业打分
    @Update("UPDATE work SET score=#{score} WHERE id=#{id}")
    void updateScore(int id, Integer score);

    //查询用户的作业
    @Select("SELECT w.taskId,w.score,t.weight FROM task t,work w WHERE t.id=w.taskId AND w.userId=#{userId} AND w.courseId=#{courseId}")
    List<WorkVO> queryWorksByIdS(int courseId, String userId);
}
