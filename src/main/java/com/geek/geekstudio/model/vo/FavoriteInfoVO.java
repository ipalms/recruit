package com.geek.geekstudio.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *封装收藏文章内容
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteInfoVO {
    private int id;
    private String courseName;
    private String adminId;
    private String adminName;
    private String image;
    private String title;
    private String articleType;
    private int likeCount;
    //private String likeStatus;
    private String favoriteTime;
}
