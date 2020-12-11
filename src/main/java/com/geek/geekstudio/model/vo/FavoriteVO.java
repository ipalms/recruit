package com.geek.geekstudio.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 封装返回前端的FavoriteBean
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteVO {
    //属性待定
    //private int id;
    private int articleId;
    private String userId;
    private String favoriteTime;
}
