package com.geek.geekstudio.model.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *PO用户类，对应数据库articlefile表
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleFilePO {
    private int id;
    private Integer articleId;
    private String fileName;
    private String filePath;
    private String addTime;
}
