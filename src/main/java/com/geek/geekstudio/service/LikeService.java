package com.geek.geekstudio.service;

import com.geek.geekstudio.model.dto.LikeDTO;
import com.geek.geekstudio.model.vo.RestInfo;

public interface LikeService {

    //查询当前用户的点赞状态
    String queryLikeStatus(String userId,int articleId);

    //改变点赞状态
    RestInfo changeLikeStatus(String userId,int articleId);
}
