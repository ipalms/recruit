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
public class TaskFileVO {
    //private int id;
    //private Integer taskId;
    private String fileName;
    private String filePath;
    private String addTime;
}