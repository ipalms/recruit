package com.geek.geekstudio.model.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *方向类，对应数据库course表
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CoursePO {
    private int id;
    private int courseId;
    private String courseName;
    private String addTime;
}
