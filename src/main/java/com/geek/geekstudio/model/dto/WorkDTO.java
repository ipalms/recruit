package com.geek.geekstudio.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

/**
 *接收前端传来的work相关参数
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkDTO {
    private int id;
    private int taskId;
    private int courseId;
    private String userId;
    private String addTime;
    private Integer score;
    private List<String> userIdList;
}
