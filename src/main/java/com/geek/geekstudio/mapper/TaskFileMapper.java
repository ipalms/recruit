package com.geek.geekstudio.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskFileMapper {

    //增加task文件的上传记录
    @Insert("INSERT INTO taskfile (taskId,fileName,filePath,addTime) " +
            "VALUES (#{taskId},#{fileName},#{filePath},#{addTime})")
    void addTaskRecord(int taskId, String fileName, String filePath, String addTime);

    //计算某一条taskId对应的文件树
    @Select("SELECT COUNT(*) FROM taskfile WHERE taskId=#{taskId}")
    int queryFileCount(int taskId);

}
