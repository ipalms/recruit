package com.geek.geekstudio.mapper;

import com.geek.geekstudio.model.vo.LikeVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeMapper {

    //查询点赞状态  注意like为关键字 用``包住
    @Select("SELECT * FROM `like` where articleId=#{articleId} AND userId=#{userId}")
    LikeVO queryLikeStatus(String userId, int articleId);

    //增加点赞记录
    @Insert("INSERT INTO `like` (userId,articleId,likeTime) VALUES (#{userId},#{articleId},#{likeTime})")
    void addLikeRecord(String userId, int articleId, String likeTime);

    //减少点赞记录
    @Delete("DELETE FROM `like` WHERE articleId=#{articleId} AND userId=#{userId}")
    void deleteLikeRecord(String userId, int articleId);

    //删除一篇文章的点赞记录
    @Delete("DELETE FROM `like` WHERE articleId=#{articleId}")
    void deleteLikeRecordById(int articleId);
}
