package com.geek.geekstudio.controller;

import com.geek.geekstudio.annotaion.PassToken;
import com.geek.geekstudio.annotaion.UserLoginToken;
import com.geek.geekstudio.model.dto.LikeDTO;
import com.geek.geekstudio.model.vo.RestInfo;
import com.geek.geekstudio.service.impl.LikeServiceImpl;
import com.geek.geekstudio.service.proxy.LikeServiceProxy;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
@Data
@RequestMapping("/like")
public class LikeController {

    @Autowired
    LikeServiceProxy likeServiceProxy;

    /**
     *查询单篇文章点赞状态
     */
    @UserLoginToken
    @GetMapping("/queryLikeStatus")
    public RestInfo queryLikeStatus(String userId, int articleId){
        String likeStatus=likeServiceProxy.queryLikeStatus(userId,articleId);
        return RestInfo.success("点赞状态查询",likeStatus);
    }

    /**
     *改变单篇文章点赞状态
     */
    @UserLoginToken
    @PostMapping("/changeLikeStatus")
    public RestInfo changeLikeStatus(@RequestBody LikeDTO likeDTO){
        return likeServiceProxy.changeLikeStatus(likeDTO.getUserId(),likeDTO.getArticleId());
    }
}
