package com.geek.geekstudio.controller;

import com.geek.geekstudio.annotaion.AdminPermission;
import com.geek.geekstudio.annotaion.SuperAdminPermission;
import com.geek.geekstudio.annotaion.UserLoginToken;
import com.geek.geekstudio.model.dto.FavoriteDTO;
import com.geek.geekstudio.model.dto.LikeDTO;
import com.geek.geekstudio.model.vo.RestInfo;
import com.geek.geekstudio.service.proxy.FavoriteServiceProxy;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
@Data
@RequestMapping("/favorite")
public class FavoriteController {
    @Autowired
    FavoriteServiceProxy favoriteServiceProxy;

    /**
     *查询单篇文章收藏状态
     */
    @UserLoginToken
    @GetMapping("/queryFavoriteStatus")
    public RestInfo queryFavoriteStatus(String userId, int articleId){
        String favoriteStatus=favoriteServiceProxy.queryFavoriteStatus(userId,articleId);
        return RestInfo.success("收藏状态查询",favoriteStatus);
    }

    /**
     *改变单篇文章收藏状态
     */
    @UserLoginToken
    @PostMapping("/changeFavoriteStatus")
    public RestInfo changeFavoriteStatus(@RequestBody FavoriteDTO favoriteDTO){
        return favoriteServiceProxy.changeFavoriteStatus(favoriteDTO.getUserId(),favoriteDTO.getArticleId());
    }

    /**
     *查询个人的收藏记录
     */
    @UserLoginToken
    @GetMapping("/queryFavorites")
    public RestInfo queryFavorites(@RequestParam(name = "page",defaultValue = "1") int page,
                                @RequestParam(name = "userId") String userId,
                                @RequestParam(name = "rows",required = false,defaultValue = "10")int rows){
        return favoriteServiceProxy.queryFavorites(page,rows,userId);
    }
}
