package com.geek.geekstudio.model.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 *管理员类，对应数据库direction表
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DirectionPO {
    private int id;
    @NotNull
    private int courseId;
    @NotBlank
    private String userId;
    private String addTime;
}
