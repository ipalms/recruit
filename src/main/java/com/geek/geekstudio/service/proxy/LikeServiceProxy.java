package com.geek.geekstudio.service.proxy;

import com.geek.geekstudio.model.dto.LikeDTO;
import com.geek.geekstudio.model.vo.RestInfo;
import com.geek.geekstudio.service.LikeService;
import com.geek.geekstudio.service.impl.LikeServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class LikeServiceProxy implements LikeService {

    @Autowired
    LikeServiceImpl likeService;

    /**
     *查询当前用户的点赞状态
     */
    @Override
    public String queryLikeStatus(String userId, int articleId) {
        return likeService.queryLikeStatus(userId,articleId);
    }

    /**
     *改变点赞状态
     */
    @Override
    public RestInfo changeLikeStatus(String userId,int articleId) {
        return likeService.changeLikeStatus(userId,articleId);
    }
}
