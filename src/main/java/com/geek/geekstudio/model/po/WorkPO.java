package com.geek.geekstudio.model.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *管理员类，对应数据库work表
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkPO {
    private int id;
    private int taskId;
    private int courseId;
    private String userId;
    private String workName;
    private String workFilePath;
    private String addTime;
    private String grade;
}