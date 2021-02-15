package com.geek.geekstudio.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *文件类，接收前端参数
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskFileDTO {
    private int id;
    private String adminId;
}
