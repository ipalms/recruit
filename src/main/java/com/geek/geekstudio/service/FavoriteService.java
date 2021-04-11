package com.geek.geekstudio.service;

import com.geek.geekstudio.model.vo.RestInfo;

public interface FavoriteService {

    //查询当前用户的收藏状态
    int queryFavoriteStatus(String userId,int articleId);

    //改变当前登录用户对文章的收藏状态
    RestInfo changeFavoriteStatus(String userId, int articleId);

    //查询个人的收藏记录
    RestInfo queryFavorites(int page, int rows, String userId);
}
