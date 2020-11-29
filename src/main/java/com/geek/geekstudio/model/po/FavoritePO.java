package com.geek.geekstudio.model.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *管理员类，对应数据库favorite表
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FavoritePO {
    private int id;
    private int articleId;
    private String userId;
    private String favoriteTime;
}
