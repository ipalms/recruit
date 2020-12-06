package com.geek.geekstudio.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 *方向类用于接收前端的参数
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseDTO {

    private int id;
    @NotNull(message = "课程ID不为空")
    private Integer courseId;
    @NotBlank(message = "课程名不能为空")
    private String courseName;
    private String addTime;
}
