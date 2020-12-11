package com.geek.geekstudio.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 封装返回前端的LikeBean
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LikeVO {

    //private int id;
    private int articleId;
    private String userId;
    private String likeTime;
}
