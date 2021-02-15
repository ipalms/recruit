package com.geek.geekstudio.mapper;

import com.geek.geekstudio.model.dto.TaskDTO;
import com.geek.geekstudio.model.po.TaskPO;
import com.geek.geekstudio.model.vo.TaskVO;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskMapper {

    //发布作业，返回自增主键
    @Insert("INSERT INTO task (courseId,adminId,taskName,addTime,closeTime,commitLate,isClosed,weight)" +
            "VALUES(#{courseId},#{adminId},#{taskName},#{addTime},#{closeTime},#{commitLate},#{isClosed},#{weight})")
    @Options(useGeneratedKeys = true,keyColumn = "id",keyProperty = "id")
    int addTask(TaskPO taskPO);

    //删除一条记录
    @Delete("DELETE FROM task WHERE id=#{id}")
    void deleteByTaskId(int id);

    //查询发布task的管理员id,courseId
    @Select("SELECT id,courseId,adminId FROM task WHERE id=#{id}")
    TaskDTO queryAdminIdById(int id);

    //修改task
    @Update("UPDATE task SET courseId=#{courseId},taskName=#{taskName},closeTime=#{closeTime}," +
            "commitLate=#{commitLate},isClosed=#{isClosed},weight=#{weight} WHERE id=#{id}")
    void updateTask(@Param("id") int id,@Param("courseId") int courseId,@Param("taskName") String taskName,@Param("closeTime") String closeTime,@Param("commitLate") int commitLate,@Param("isClosed") int isClosed,@Param("weight") int weight);

    //手动关闭task
    @Update("UPDATE task SET commitLate=0,isClosed=1 WHERE id=#{id}")
    void closeTask(int id);

    //查询符合关闭提交作业通道的记录
    @Select("SELECT * FROM task WHERE closeTime<#{closeTime} AND commitLate=0 AND isClosed=0")
    List<TaskPO> queryTaskList(String closeTime);

    //定时任务关闭task
    @Update("UPDATE task SET isClosed=1 WHERE id=#{id}")
    void shutUpCommit(int id);

    //查询某个课程下的所有发布的作业（task）
    @Select("SELECT t.id,t.adminId,t.taskName,t.addTime,t.closeTime,t.commitLate,t.isClosed," +
            "t.weight FROM task t WHERE courseId=#{courseId}")
    List<TaskVO> queryTasksByCourseId(int courseId);

    //根据id查询一条task记录
    @Select("SELECT * FROM task WHERE id=#{id}")
    TaskPO queryOneTaskById(int id);

    //查询某个管理员发布的所有发布的作业（task）
    @Select("SELECT * FROM task t WHERE adminId=#{adminId}")
    List<TaskVO> queryTasksByAdminId(String adminId);

}
