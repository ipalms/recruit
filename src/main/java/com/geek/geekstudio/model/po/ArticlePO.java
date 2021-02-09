package com.geek.geekstudio.model.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *管理员类，对应数据库article表  no use now
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticlePO {
    private int id;
    private int courseId;
    private String userId;
    private String title;
    private String content;
    private String addTime;
    private String articleType;
    private int likeCount;
}
