package com.geek.geekstudio.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 *封装前端的direction相关数据
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DirectionDTO {
    private int id;
    @NotNull(message = "方向ID不能为空！")
    private int courseId;
    @NotBlank@NotNull(message = "用户ID不能为空！")
    private String userId;
    private String addTime;
}
