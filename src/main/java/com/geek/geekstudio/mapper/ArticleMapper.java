package com.geek.geekstudio.mapper;

import com.geek.geekstudio.model.dto.ArticleDTO;
import com.geek.geekstudio.model.vo.ArticleInfoVO;
import com.geek.geekstudio.model.vo.FavoriteInfoVO;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleMapper {

    //发布文章-- 返回递增的主键值
    @Insert("INSERT INTO article (userId,courseId,title,content,addTime,likeCount,articleType)" +
            "VALUES(#{userId},#{courseId},#{title},#{content},#{addTime},#{likeCount},#{articleType})")
    @Options(useGeneratedKeys = true,keyColumn = "id",keyProperty = "id")
    int addArticle(ArticleDTO articleDTO);

    //查询文章总数量（一定条件下）
    int queryArticleTotal(@Param("adminName") String adminName,@Param("courseName") String courseName);

    //查询文章的具体信息（一定条件下）
    List<ArticleInfoVO> queryArticle(@Param("adminName") String adminName,@Param("courseName")  String courseName, @Param("start")int start,@Param("rows") int rows);

    //查询一篇文章的明细
    ArticleInfoVO queryArticleById(@Param("id") int id);

    //改变文章的点赞数属性
    @Update("UPDATE article SET likeCount=likeCount+#{variation} WHERE id=#{id}")
    void changeLikeCount(@Param("id") int id,@Param("variation") int variation);

    //查询收藏的文章部分信息
    FavoriteInfoVO queryFavoriteArticle(@Param("id") int id);

    //删除一篇文章
    @Delete("DELETE FROM article WHERE id=#{id}")
    void deleteByArticleId(int id);
}
