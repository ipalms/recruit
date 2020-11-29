package com.geek.geekstudio.model.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *管理员类，对应数据库article表
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticlePO {
    private int id;
    private String userId;
    private String title;
    private String content;
    private String fileName;
    private String filePath;
    private String addTime;
    private int likeCount;
}
