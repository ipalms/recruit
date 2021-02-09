package com.geek.geekstudio.model.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *PO文件类，对应数据库taskfile表  no use now
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskFilePO {
    private int id;
    private Integer taskId;
    private String fileName;
    private String filePath;
    private String addTime;
}