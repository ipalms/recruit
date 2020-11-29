package com.geek.geekstudio.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 封装返回前端的AnnounceBean
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnnounceVO {
    //属性待定
    private int id;
    private String adminId;
    private String title;
    private String content;
    private String addTime;
    private String fileName;
    private String filePath;
}
