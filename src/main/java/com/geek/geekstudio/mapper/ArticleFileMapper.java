package com.geek.geekstudio.mapper;

import com.geek.geekstudio.model.vo.ArticleFileVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleFileMapper {

    //增加文章附件的上传记录
    @Insert("INSERT INTO articlefile (articleId,fileName,filePath,addTime) " +
            "VALUES (#{articleId},#{fileName},#{filePath},#{addTime})")
    void addUploadRecord(int articleId, String fileName, String filePath, String addTime);

    //查询文章附件记录
    @Select("SELECT * FROM articlefile WHERE articleId=#{articleId}")
    List<ArticleFileVO> queryFilesByArticleId(int articleId);

    //删除一篇文章对应的文件
    @Delete("DELETE FROM articlefile WHERE articleId=#{articleId}")
    void deleteFilesByArticleId(int articleId);
}
