package com.geek.geekstudio.service.proxy;

import com.geek.geekstudio.exception.ParameterError;
import com.geek.geekstudio.exception.PermissionDeniedException;
import com.geek.geekstudio.exception.RecruitFileException;
import com.geek.geekstudio.model.dto.ArticleDTO;
import com.geek.geekstudio.model.vo.RestInfo;
import com.geek.geekstudio.service.ArticleService;
import com.geek.geekstudio.service.impl.ArticleServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Slf4j
public class ArticleServiceProxy implements ArticleService {
    @Autowired
    ArticleServiceImpl articleService;

    /**
     *文章发布
     */
    @Override
    public RestInfo addArticle(ArticleDTO articleDTO) {
        log.info("用户 "+articleDTO.getUserId()+" 发表了一篇文章"+",文章类型:"+articleDTO.getArticleType());
        return articleService.addArticle(articleDTO);
    }

    /**
     *查询文章  可根据发布者名字或方向名查询
     */
    @Override
    public RestInfo queryArticles(int page, int rows, String adminName, String courseName, String userId) {
        return articleService.queryArticles(page,rows,adminName,courseName,userId);
    }

    /**
     *查询一篇文章详细内容
     */
    @Override
    public RestInfo queryOneArticle(int articleId,String userId) throws RecruitFileException {
        return articleService.queryOneArticle(articleId,userId);
    }

    /**
     * 查询自己发表的文章
     */
    @Override
    public RestInfo queryMyArticles(int page, int rows, String userId) {
        return articleService.queryMyArticles(page, rows, userId);
    }

    /**
     * 删除一篇发布的文章
     */
    @Override
    public RestInfo deleteArticle(ArticleDTO articleDTO) throws ParameterError, PermissionDeniedException {
        log.info("管理员"+articleDTO.getUserId()+" 删除了id为"+articleDTO.getId()+"了一篇文章");
        return articleService.deleteArticle(articleDTO);
    }
}
