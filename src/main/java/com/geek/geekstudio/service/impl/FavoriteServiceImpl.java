package com.geek.geekstudio.service.impl;

import com.geek.geekstudio.annotaion.AdminPermission;
import com.geek.geekstudio.mapper.ArticleMapper;
import com.geek.geekstudio.mapper.FavoriteMapper;
import com.geek.geekstudio.model.po.FavoritePO;
import com.geek.geekstudio.model.vo.FavoriteInfoVO;
import com.geek.geekstudio.model.vo.FavoriteVO;
import com.geek.geekstudio.model.vo.PageListVO;
import com.geek.geekstudio.model.vo.RestInfo;
import com.geek.geekstudio.service.FavoriteService;
import com.geek.geekstudio.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class FavoriteServiceImpl implements FavoriteService {

    @Autowired
    FavoriteMapper favoriteMapper;

    @Autowired
    ArticleMapper articleMapper;

    /**
     *查询收藏状态
     */
    @Override
    public String queryFavoriteStatus(String userId, int articleId) {
        FavoriteVO favoriteVO=favoriteMapper.queryFavoriteStatus(userId,articleId);
        return favoriteVO==null?"未收藏":"已收藏";
    }

    /**
     *改变单篇文章收藏状态
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestInfo changeFavoriteStatus(String userId, int articleId) {
        FavoriteVO favoriteVO=favoriteMapper.queryFavoriteStatus(userId,articleId);
        if(favoriteVO==null){
            favoriteMapper.addFavoriteRecord(userId,articleId, DateUtil.creatDate());
        }else {
            favoriteMapper.deleteFavoriteRecord(userId,articleId);
        }
        return RestInfo.success("改变收藏状态成功！");
    }

    /**
     *查询个人的收藏记录
     */
    @Override
    public RestInfo queryFavorites(int page, int rows, String userId) {
        int total=favoriteMapper.queryFavoritesTotal(userId);
        int start=(page-1)*rows;
        //先取出个人的收藏记录
        List<FavoriteVO> favoriteVOList=favoriteMapper.queryMyFavorites(userId,start,rows);
        List<FavoriteInfoVO> favoriteInfoVOList=new ArrayList<FavoriteInfoVO>();
        //读取记录对应的文章
        if(favoriteVOList!=null) {
            for (FavoriteVO favoriteVO : favoriteVOList) {
                FavoriteInfoVO favoriteInfoVO = articleMapper.queryFavoriteArticle(favoriteVO.getArticleId());
                favoriteInfoVO.setFavoriteTime(favoriteVO.getFavoriteTime());
                favoriteInfoVOList.add(favoriteInfoVO);
            }
        }
        int totalPage=total%rows==0?total/rows:total/rows+1;
        return RestInfo.success(new PageListVO(total,page,totalPage,rows,favoriteInfoVOList));
    }
}
