package com.geek.geekstudio.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *work文件类，接收前端参数
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkFileDTO {
    private int id;
    private String userId;
}
