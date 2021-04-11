package com.geek.geekstudio.service.proxy;

import com.geek.geekstudio.model.vo.RestInfo;
import com.geek.geekstudio.service.FavoriteService;
import com.geek.geekstudio.service.impl.FavoriteServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class FavoriteServiceProxy implements FavoriteService {

    @Autowired
    FavoriteServiceImpl favoriteService;

    /**
     *查询收藏状态
     */
    @Override
    public int queryFavoriteStatus(String userId, int articleId) {
        return favoriteService.queryFavoriteStatus(userId,articleId);
    }

    /**
     *改变单篇文章收藏状态
     */
    @Override
    public RestInfo changeFavoriteStatus(String userId, int articleId) {
        return favoriteService.changeFavoriteStatus(userId,articleId);
    }

    /**
     *查询个人的收藏记录
     */
    @Override
    public RestInfo queryFavorites(int page, int rows, String userId) {
        return favoriteService.queryFavorites(page,rows,userId);
    }
}
