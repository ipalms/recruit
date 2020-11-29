package com.geek.geekstudio.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 封装返回前端的DirectionBean
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DirectionVO {
    private int id;
    private int courseId;
    private String useId;
    private String courseName;
    private String addTime;
}
