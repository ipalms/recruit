package com.geek.geekstudio.model.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *管理员类，对应数据库task表
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskPO {
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
