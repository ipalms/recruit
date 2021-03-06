package com.geek.geekstudio.model.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *PO文件类，对应数据库workfile表
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkFilePO {
    private int id;
    private Integer workId;
    private String fileName;
    private String filePath;
    private String addTime;
}