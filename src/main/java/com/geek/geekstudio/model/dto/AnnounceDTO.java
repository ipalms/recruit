package com.geek.geekstudio.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *公告类，接收前端参数
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnnounceDTO {
    private int id;
    private int courseId;
    private String adminId;
    private String title;
    private String content;
    private String addTime;
    private String fileName;
    private String filePath;
}
