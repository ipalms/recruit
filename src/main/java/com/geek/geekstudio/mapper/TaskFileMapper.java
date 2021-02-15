package com.geek.geekstudio.mapper;

import com.geek.geekstudio.model.po.TaskFilePO;
import com.geek.geekstudio.model.po.TaskPO;
import com.geek.geekstudio.model.vo.TaskFileVO;
import com.geek.geekstudio.model.vo.TaskVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskFileMapper {

    //增加task文件的上传记录
    @Insert("INSERT INTO taskfile (taskId,fileName,filePath,addTime) " +
            "VALUES (#{taskId},#{fileName},#{filePath},#{addTime})")
    void addTaskRecord(int taskId, String fileName, String filePath, String addTime);

    //计算某一条taskId对应的文件数
    @Select("SELECT COUNT(*) FROM taskfile WHERE taskId=#{taskId}")
    int queryFileCount(int taskId);

    //删除task文件记录
    @Delete("DELETE FROM taskfile WHERE taskId=#{taskId}")
    void deleteFilesByTaskId(int taskId);

    //通过taskId查询
    @Select("SELECT fileName,filePath,addTime FROM taskfile  WHERE taskId=#{taskId}")
    List<TaskFileVO> queryTaskFileById(int taskId);

    //等值连接查询task完整记录
    @Select("SELECT task.*,taskfile.filePath from task,taskfile WHERE task.id=taskfile.taskId AND taskfile.id=#{id}")
    TaskVO queryTaskById(int id);

    //通过id删除文件
    @Delete("DELETE FROM taskfile WHERE id=#{id}")
    void deleteFileById(int id);

    //根据filePath找文件记录
    @Select("SELECT * FROM taskfile WHERE filePath=#{filePath}")
    TaskFilePO queryTaskFileByFilePath(String filePath);

    //更改记录上传的时间
    @Update("UPDATE taskfile SET addTime=#{addTime} WHERE id=#{id}")
    void updateRecordTime(int id, String addTime);
}
