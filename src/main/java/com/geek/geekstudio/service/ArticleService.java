package com.geek.geekstudio.service;

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
}
