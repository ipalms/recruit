package com.geek.geekstudio.model.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *公告类，对应数据库announce表 no use now
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnnouncePO {
    private int id;
    private int courseId;
    private String adminId;
    private String title;
    private String content;
    private String addTime;
    private String fileName;
    private String filePath;
}
