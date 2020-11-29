package com.geek.geekstudio.model.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *管理员类，对应数据库direction表
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DirectionPO {
    private int id;
    private int courseId;
    private String useId;
    private String courseName;
    private String addTime;
}
