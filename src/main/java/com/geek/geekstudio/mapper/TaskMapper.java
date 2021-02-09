package com.geek.geekstudio.mapper;

import com.geek.geekstudio.model.po.TaskPO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

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
}
