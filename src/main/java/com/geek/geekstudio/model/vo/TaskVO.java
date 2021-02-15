package com.geek.geekstudio.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 封装返回前端的TaskBean
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskVO {
    private int id;
    private int courseId;
    private String adminId;
    private String taskName;
    private String addTime;
    private String closeTime;
    private int commitLate;
    private int isClosed;
    private int weight;
    private List<TaskFileVO> taskFileVOList; //task记录对应的文件
    private String filePath;
}
