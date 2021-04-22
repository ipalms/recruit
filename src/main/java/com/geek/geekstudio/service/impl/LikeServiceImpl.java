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
    public int queryLikeStatus(String userId, int articleId) {
        LikeVO likeVO=likeMapper.queryLikeStatus(userId,articleId);
        return likeVO==null?0:1;
    }

    /**
     *改变点赞状态 更改过快可能会导致数据库插入重复数据--加入唯一索引
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestInfo changeLikeStatus(String userId,int articleId) {
        LikeVO likeVO=likeMapper.queryLikeStatus(userId,articleId);
        int variation;
        if(likeVO==null){
            try {
                likeMapper.addLikeRecord(userId,articleId, DateUtil.creatDate());
                variation=1;
            } catch (Exception e) {
                likeMapper.deleteLikeRecord(userId,articleId);
                return RestInfo.success("改变点赞状态成功！");
            }
        }else {
            likeMapper.deleteLikeRecord(userId,articleId);
            variation=-1;
        }
        //改变文章点赞量
        articleMapper.changeLikeCount(articleId,variation);
        return RestInfo.success("改变点赞状态成功！");
    }
}
