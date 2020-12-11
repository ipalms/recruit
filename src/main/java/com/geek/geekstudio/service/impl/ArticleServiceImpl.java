package com.geek.geekstudio.service.impl;

import com.geek.geekstudio.exception.RecruitFileException;
import com.geek.geekstudio.mapper.ArticleFileMapper;
import com.geek.geekstudio.mapper.ArticleMapper;
import com.geek.geekstudio.mapper.LikeMapper;
import com.geek.geekstudio.model.dto.ArticleDTO;
import com.geek.geekstudio.model.vo.ArticleFileVO;
import com.geek.geekstudio.model.vo.ArticleInfoVO;
import com.geek.geekstudio.model.vo.PageListVO;
import com.geek.geekstudio.model.vo.RestInfo;
import com.geek.geekstudio.service.ArticleService;
import com.geek.geekstudio.util.DateUtil;
import com.geek.geekstudio.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Service
@Slf4j
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    ArticleMapper articleMapper;

    @Autowired
    ArticleFileMapper articleFileMapper;

    @Autowired
    LikeServiceImpl likeService;

    @Autowired
    FavoriteServiceImpl favoriteService;

    @Autowired
    FileUtil fileUtil;

    /**
     *文章发布 返回新增文章的ID
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public RestInfo addArticle(ArticleDTO articleDTO) {
        articleDTO.setAddTime(DateUtil.creatDate());
        articleMapper.addArticle(articleDTO);
        return RestInfo.success("文章发表成功",articleDTO.getId());
    }

    /**
     *查询文章  可根据发布者名字或方向名查询
     */
    @Override
    public RestInfo queryArticles(int page, int rows, String adminName, String courseName, String userId) {
        int total=articleMapper.queryArticleTotal(adminName,courseName);
        int start=(page-1)*rows;
        List<ArticleInfoVO> articleInfoVOList=articleMapper.queryArticle(adminName,courseName,start,rows);
        if(userId!=null) {
            for (ArticleInfoVO articleInfoVO : articleInfoVOList) {
                //如果此时用户已登录，可查看点赞、收藏状态
                String likeStatus=likeService.queryLikeStatus(userId, articleInfoVO.getId());
                String favoriteStatus=favoriteService.queryFavoriteStatus(userId,articleInfoVO.getId());
                articleInfoVO.setLikeStatus(likeStatus);
                articleInfoVO.setFavoriteStatus(favoriteStatus);
            }
        }
        int totalPage=total%rows==0?total/rows:total/rows+1;
        return RestInfo.success(new PageListVO(total,page,totalPage,rows,articleInfoVOList));
    }

    /**
     *查询一篇文章详细内容   目前和查询所有文章使用同一个对象封装返回数据
     */
    @Override
    public RestInfo queryOneArticle(int articleId, String articleType) throws RecruitFileException {
        String fileStorageLocation=null;
        List<ArticleFileVO> articleFileVOList=null;
        if(articleType.equals("md")){
            articleFileVOList=articleFileMapper.queryFilesByArticleId(articleId);
            fileStorageLocation=fileUtil.getFileStorageLocation().toString();
            for(ArticleFileVO articleFileVO:articleFileVOList){
                try {
                    //获得markdown文件的内容（纯文本）
                    String filePath=articleFileVO.getFilePath();
                    String richText = Files.readString(Paths.get(fileStorageLocation+filePath));
                    articleFileVO.setRichText(richText);
                } catch (IOException e) {
                    throw new RecruitFileException("读取markdown文件失败");
                }
            }
        }
        ArticleInfoVO articleInfoVO=articleMapper.queryArticleById(articleId);
        articleInfoVO.setArticleFileVOList(articleFileVOList);
        return RestInfo.success("文章详细内容",articleInfoVO);
    }
}
