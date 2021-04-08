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
    private Integer courseId;
    private String adminId;
    private String title;
    private String content;
    private String addTime;
    private String fileName;
    private String filePath;

    public AnnouncePO(Integer courseId, String adminId, String title, String content, String addTime, String fileName, String filePath) {
        this.courseId = courseId;
        this.adminId = adminId;
        this.title = title;
        this.content = content;
        this.addTime = addTime;
        this.fileName = fileName;
        this.filePath = filePath;
    }
}
