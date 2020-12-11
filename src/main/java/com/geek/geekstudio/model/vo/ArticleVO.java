package com.geek.geekstudio.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 封装返回前端的ArticleBean
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleVO {
    //属性待定
    private int id;
    private int courseId;
    private String userId;
    private String title;
    private String content;
    private String addTime;
    private String articleType;
    private int likeCount;
}
