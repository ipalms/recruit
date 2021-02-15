package com.geek.geekstudio.service;

import com.geek.geekstudio.exception.ParameterError;
import com.geek.geekstudio.exception.PermissionDeniedException;
import com.geek.geekstudio.exception.RecruitFileException;
import com.geek.geekstudio.model.dto.ArticleDTO;
import com.geek.geekstudio.model.vo.RestInfo;

import java.io.IOException;

public interface ArticleService {

    //文章发布
    RestInfo addArticle(ArticleDTO articleDTO);

    //文章查询
    RestInfo queryArticles(int page, int rows, String adminName, String courseName ,String userId);

    //查询一篇文章的详情
    RestInfo queryOneArticle(int articleId, String articleType) throws RecruitFileException;

    //查询自己发布的文章
    RestInfo queryMyArticles(int page, int rows, String userId);

    //删除一篇自己发布的文章
    RestInfo deleteArticle(ArticleDTO articleDTO) throws ParameterError, PermissionDeniedException;
}
