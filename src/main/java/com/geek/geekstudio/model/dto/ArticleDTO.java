package com.geek.geekstudio.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 封装返回前端的文章数据
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleDTO {
    private int id;
    private Integer courseId;
    private String userId;
    private String title;
    private String content;
    private String addTime;
    private String articleType;
    private int likeCount;
}
