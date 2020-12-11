package com.geek.geekstudio.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 封装前端的Favorite相关参数
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteDTO {

    //private int id;
    private int articleId;
    private String userId;
    private String favoriteTime;
}
