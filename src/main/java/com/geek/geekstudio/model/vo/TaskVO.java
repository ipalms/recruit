package com.geek.geekstudio.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 封装返回前端的TaskBean
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskVO {
    //属性待定
    private int id;
    private int courseId;
    private String adminId;
    private String taskName;
    private String taskFileName;
    private String taskFilePath;
    private String addTime;
    private String closeTime;
    private String isClosed;
}
