package com.geek.geekstudio.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *返回前端的类信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleFileVO {
    //private int id;
    private Integer articleId;
    private String fileName;
    private String filePath;
    private String richText;
    //private String addTime;
}
