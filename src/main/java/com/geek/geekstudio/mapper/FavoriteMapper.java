package com.geek.geekstudio.mapper;

import com.geek.geekstudio.model.vo.FavoriteVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoriteMapper {

    //查询收藏状态
    @Select("SELECT * FROM favorite where articleId=#{articleId} AND userId=#{userId}")
    FavoriteVO queryFavoriteStatus(String userId, int articleId);

    //增加收藏记录
    @Insert("INSERT INTO favorite (userId,articleId,favoriteTime) VALUES (#{userId},#{articleId},#{favoriteTime})")
    void addFavoriteRecord(String userId, int articleId, String favoriteTime);

    //减少收藏记录
    @Delete("DELETE FROM favorite WHERE userId=#{userId} AND articleId=#{articleId}")
    void deleteFavoriteRecord(String userId, int articleId);

    //查询个人的收藏总记录
    @Select("SELECT COUNT(*) FROM favorite WHERE userId=#{userId}")
    int queryFavoritesTotal(String userId);

    //查询个人收藏记录的最新rows条
    @Select("SELECT * FROM favorite WHERE userId=#{userId} ORDER BY id DESC LIMIT #{start},#{rows}")
    List<FavoriteVO> queryMyFavorites(@Param("userId") String userId,@Param("start")  int start,@Param("rows")int rows);

    //删除一篇文章对应的收藏记录
    @Delete("DELETE FROM favorite WHERE articleId=#{articleId}")
    void deleteFavoriteRecordById(int articleId);
}
