package com.geek.geekstudio.model.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *PO文件类，对应数据库articlefile表  no use now
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
