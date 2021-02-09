package com.geek.geekstudio.mapper;

import com.geek.geekstudio.model.po.AnnouncePO;
import com.geek.geekstudio.model.vo.AnnounceVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnnounceMapper {

    //发布公告
    @Insert("INSERT INTO announce (courseId,adminId,title,content,addTime,fileName,filePath) " +
            "VALUES (#{courseId},#{adminId},#{title},#{content},#{addTime},#{fileName},#{filePath})")
    void addAnnounce(@Param("courseId") int courseId,@Param("adminId") String adminId,@Param("title") String title,@Param("content") String content,@Param("addTime") String addTime,@Param("fileName") String fileName,@Param("filePath") String filePath);

    //查询一条公告记录
    @Select("SELECT * FROM announce WHERE id=#{id}")
    AnnouncePO findAnnounceById(int id);

    //删除公告
    @Delete("DELETE FROM announce WHERE id=#{id}")
    void delAnnounce(int id);

    //查询总记录数
    @Select("SELECT COUNT(*) FROM announce WHERE 1=1 AND (courseId = #{courseId} or #{courseId}=0)")
    int queryAnnounceTotal(int courseId);

    //查询分页的公告记录
    @Select("SELECT id,courseId,adminId,title,addTime FROM announce WHERE 1=1 AND (courseId = #{courseId} or #{courseId}=0) ORDER BY id DESC LIMIT #{start},#{rows}")
    List<AnnounceVO> queryAnnounceList(@Param("courseId") int courseId,@Param("start") int start,@Param("rows") int rows);
}
