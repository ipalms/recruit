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
public class WorkFileVO {
    private int id;
    private Integer workId;
    private String fileName;
    private String filePath;
    private String addTime;
}