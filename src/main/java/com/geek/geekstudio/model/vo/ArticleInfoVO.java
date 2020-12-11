package com.geek.geekstudio.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

//封装文章的具体信息
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleInfoVO {
    private int id;
    private String courseName;
    private String adminId;
    private String adminName;
    private String image;
    private String title;
    private String content;
    private String addTime;
    private String articleType;
    private int likeCount;
    private String likeStatus;
    private String favoriteStatus;
    private List<ArticleFileVO> articleFileVOList;
}
