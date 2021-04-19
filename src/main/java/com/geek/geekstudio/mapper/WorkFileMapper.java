package com.geek.geekstudio.mapper;

import com.geek.geekstudio.model.po.WorkFilePO;
import com.geek.geekstudio.model.vo.WorkFileVO;
import com.geek.geekstudio.model.vo.WorkVO;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkFileMapper {

    //增加提交作业的记录
    @Insert("INSERT INTO workfile (workId,fileName,filePath,addTime)"+
                    "VALUES (#{workId},#{fileName},#{filePath},#{addTime})")
    void addWorkRecord(int workId, String fileName, String filePath, String addTime);

    //计算某一条workId对应的文件数
    @Select("SELECT COUNT(*) FROM workfile WHERE workId=#{workId}")
    int queryFileCount(int workId);

    //等值连接查询work完整记录
    @Select("SELECT work.*,workfile.filePath from work,workfile WHERE work.id=workfile.workId AND workfile.id=#{id}")
    WorkVO queryWorkById(int id);

    //通过id删除文件
    @Delete("DELETE FROM workfile WHERE id=#{id}")
    void deleteFileById(int id);

    //根据filePath找文件记录
    @Select("SELECT * FROM workfile WHERE filePath=#{filePath}")
    WorkFilePO queryWorkFileByFilePath(String filePath);

    //更改记录上传的时间
    @Update("UPDATE workfile SET addTime=#{addTime} WHERE id=#{id}")
    void updateRecordTime(int id, String addTime);

    //删除work文件记录
    @Delete("DELETE FROM workfile WHERE workId=#{workId}")
    void deleteFilesByWorkId(int workId);

    //根据workId查询记录
    @Select("SELECT id,fileName,filePath,addTime FROM workfile  WHERE workId=#{workId}")
    List<WorkFileVO> queryFilesByWorkId(int workId);
}
