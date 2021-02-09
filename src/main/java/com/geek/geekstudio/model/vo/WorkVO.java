package com.geek.geekstudio.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 封装返回前端的WorkBean no use now
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkVO {
    //属性待定
    private int id;
    private int taskId;
    private int courseId;
    private String userId;
    private String addTime;
    private String grade;
}
