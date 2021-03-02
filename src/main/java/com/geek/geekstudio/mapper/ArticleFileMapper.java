package com.geek.geekstudio.mapper;

import com.geek.geekstudio.model.po.ArticleFilePO;
import com.geek.geekstudio.model.vo.ArticleFileVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
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

    //根据地址查询article文件
    @Select("SELECT * FROM articlefile WHERE filePath=#{filePath}")
    ArticleFilePO queryArticleFileByFilePath(String filePath);

    //更改记录上传的时间
    @Update("UPDATE articlefile SET addTime=#{addTime} WHERE id=#{id}")
    void updateRecordTime(int id, String addTime);
}
