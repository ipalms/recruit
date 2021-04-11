package com.geek.geekstudio.service.impl;

import com.geek.geekstudio.exception.ParameterError;
import com.geek.geekstudio.exception.PermissionDeniedException;
import com.geek.geekstudio.exception.RecruitFileException;
import com.geek.geekstudio.mapper.ArticleFileMapper;
import com.geek.geekstudio.mapper.ArticleMapper;
import com.geek.geekstudio.mapper.FavoriteMapper;
import com.geek.geekstudio.mapper.LikeMapper;
import com.geek.geekstudio.model.dto.ArticleDTO;
import com.geek.geekstudio.model.po.ArticlePO;
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

import java.io.File;
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
    LikeMapper likeMapper;

    @Autowired
    FavoriteMapper favoriteMapper;

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
        if(userId!=null&&articleInfoVOList!=null) {
            for (ArticleInfoVO articleInfoVO : articleInfoVOList) {
                //如果此时用户已登录，可查看点赞、收藏状态   文章点赞状态分开查询了
                int likeStatus=likeService.queryLikeStatus(userId, articleInfoVO.getId());
                int favoriteStatus=favoriteService.queryFavoriteStatus(userId,articleInfoVO.getId());
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
            if(articleFileVOList!=null) {
                for (ArticleFileVO articleFileVO : articleFileVOList) {
                    try {
                        //获得markdown文件的内容（纯文本）
                        String filePath = articleFileVO.getFilePath();
                        String richText = Files.readString(Paths.get(fileStorageLocation + filePath));
                        articleFileVO.setRichText(richText);
                    } catch (IOException e) {
                        throw new RecruitFileException("读取markdown文件失败");
                    }
                }
            }
        }
        ArticleInfoVO articleInfoVO=articleMapper.queryArticleById(articleId);
        articleInfoVO.setArticleFileVOList(articleFileVOList);
        return RestInfo.success("文章详细内容",articleInfoVO);
    }

    /**
     * 查询自己发表的文章
     */
    @Override
    public RestInfo queryMyArticles(int page, int rows, String userId) {
        int total=articleMapper.queryMyTotal(userId);
        int start=(page-1)*rows;
        List<ArticleInfoVO> articleInfoVOList=articleMapper.queryMyArticles(userId,start,rows);
        for (ArticleInfoVO articleInfoVO : articleInfoVOList) {
            int likeStatus=likeService.queryLikeStatus(userId, articleInfoVO.getId());
            int favoriteStatus=favoriteService.queryFavoriteStatus(userId,articleInfoVO.getId());
            articleInfoVO.setLikeStatus(likeStatus);
            articleInfoVO.setFavoriteStatus(favoriteStatus);
        }
        int totalPage=total%rows==0?total/rows:total/rows+1;
        return RestInfo.success(new PageListVO(total,page,totalPage,rows,articleInfoVOList));
    }

    /**
     * 删除一篇发布的文章
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestInfo deleteArticle(ArticleDTO articleDTO) throws ParameterError, PermissionDeniedException {
        int id=articleDTO.getId();
        ArticlePO articlePO=articleMapper.queryOneArticleById(id);
        if(articlePO==null){
            throw new ParameterError();
        }
        if(!articlePO.getUserId().equals(articleDTO.getUserId())){
            throw new PermissionDeniedException();
        }
        if("md".equals(articlePO.getArticleType())){
            articleFileMapper.deleteFilesByArticleId(id);
            fileUtil.deleteDir(new File(fileUtil.buildArticleFilePath(id)));
        }
        likeMapper.deleteLikeRecordById(id);
        favoriteMapper.deleteFavoriteRecordById(id);
        articleMapper.deleteByArticleId(id);
        return RestInfo.success("删除一篇文章记录成功！",null);
    }
}
