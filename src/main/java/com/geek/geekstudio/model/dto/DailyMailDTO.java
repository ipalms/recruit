package com.geek.geekstudio.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 *用于接收前端传来的日常邮件参数
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DailyMailDTO {
    private String adminId;
    private Integer courseId;
    private List<String> userIdList;
    private String title;
    private String text;
}
