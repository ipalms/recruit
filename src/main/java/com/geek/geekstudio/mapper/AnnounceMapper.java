package com.geek.geekstudio.mapper;

import com.geek.geekstudio.model.po.AnnouncePO;
import com.geek.geekstudio.model.po.FragmentFilePO;
import com.geek.geekstudio.model.vo.AnnounceVO;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnnounceMapper {

    //发布公告（int类型不能插入null）
    @Insert("INSERT INTO announce (courseId,adminId,title,content,addTime,fileName,filePath) " +
            "VALUES (#{courseId},#{adminId},#{title},#{content},#{addTime},#{fileName},#{filePath})")
    @Options(useGeneratedKeys = true,keyColumn = "id",keyProperty = "id")
    void addAnnounce(AnnouncePO announcePO);

    //查询一条公告记录
    @Select("SELECT * FROM announce WHERE id=#{id}")
    AnnouncePO findAnnounceById(int id);

    //删除公告
    @Delete("DELETE FROM announce WHERE id=#{id}")
    void delAnnounce(int id);

    //查询总记录数 (1=1 AND可删)
    @Select("SELECT COUNT(*) FROM announce WHERE (courseId = #{courseId} or #{courseId}=0)")
    int queryAnnounceTotal(int courseId);

    //查询分页的公告记录
    @Select("SELECT id,courseId,adminId,title,addTime FROM announce WHERE (courseId = #{courseId} or #{courseId}=0) ORDER BY id DESC LIMIT #{start},#{rows}")
    List<AnnounceVO> queryAnnounceList(@Param("courseId") int courseId,@Param("start") int start,@Param("rows") int rows);

/*    //根据名称查询断点续传文件是否存在
    @Select("SELECT * FROM fragmentfile WHERE fileName =#{fileName}")
    FragmentFilePO queryFileByName(String fileName);*/

    //修改断点续传文件文件信息
    @Update("UPDATE fragmentfile SET shardIndex=#{shardIndex},updatedTime=#{updatedTime} WHERE id =#{id}")
    void updateFileInfo(int id, int shardIndex, String updatedTime);

    //保存第一次存入的断点续传文件文件信息
    @Insert("INSERT INTO fragmentfile (filePath,fileSize,createdTime,updatedTime,shardIndex,shardTotal,fileKey)" +
            "VALUES (#{filePath},#{fileSize},#{createdTime},#{updatedTime},#{shardIndex},#{shardTotal},#{fileKey})")
    void insertFileInfo(String filePath, Integer fileSize, String createdTime, String updatedTime, Integer shardIndex, Integer shardTotal, String fileKey);

    //根据key值查询断点续传文件是否存在
    @Select("SELECT * FROM fragmentfile WHERE fileKey=#{fileKey}")
    FragmentFilePO queryFileByKey(String fileKey);

    //更新文件状态
    @Update("UPDATE fragmentfile SET filePath=#{filePath},updatedTime=#{updatedTime} WHERE id =#{id}")
    void updateFilePath(int id, String filePath, String updatedTime);

    //合并完成后添加file记录到announce表中
    @Update("UPDATE announce SET fileName=#{fileName},filePath=#{filePath} WHERE id =#{id}")
    void updateAnnounceFile(int id, String fileName, String filePath);

    //删除公告
    @Delete("DELETE FROM fragmentfile WHERE filePath=#{filePath}")
    void delFragmentRecord(String filePath);
}
