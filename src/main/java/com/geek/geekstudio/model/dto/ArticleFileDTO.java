package com.geek.geekstudio.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *接收前端的参数
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleFileDTO {
    private int id;
    private Integer articleId;
    private String fileName;
    private String filePath;
    private String addTime;
}
