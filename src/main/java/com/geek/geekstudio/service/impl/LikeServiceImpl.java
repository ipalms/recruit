package com.geek.geekstudio.service.impl;

import com.geek.geekstudio.mapper.ArticleMapper;
import com.geek.geekstudio.mapper.LikeMapper;
import com.geek.geekstudio.model.dto.LikeDTO;
import com.geek.geekstudio.model.vo.LikeVO;
import com.geek.geekstudio.model.vo.RestInfo;
import com.geek.geekstudio.service.LikeService;
import com.geek.geekstudio.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class LikeServiceImpl implements LikeService {
    @Autowired
    LikeMapper likeMapper;
    @Autowired
    ArticleMapper articleMapper;

    /**
     *查询当前用户的点赞状态
     */
    @Override
    public String queryLikeStatus(String userId, int articleId) {
        LikeVO likeVO=likeMapper.queryLikeStatus(userId,articleId);
        return likeVO==null?"未点赞":"已点赞";
    }

    /**
     *改变点赞状态
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestInfo changeLikeStatus(String userId,int articleId) {
        LikeVO likeVO=likeMapper.queryLikeStatus(userId,articleId);
        int variation=0;
        if(likeVO==null){
            likeMapper.addLikeRecord(userId,articleId, DateUtil.creatDate());
            variation=1;
        }else {
            likeMapper.deleteLikeRecord(userId,articleId);
            variation=-1;
        }
        //改变文章点赞量
        articleMapper.changeLikeCount(articleId,variation);
        return RestInfo.success("改变点赞状态成功！");
    }
}
