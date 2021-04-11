package com.geek.geekstudio.model.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FragmentFilePO {
    private int id;
    //相对路径
    private String filePath;
/*    //文件名
    private String fileName;*/
    //文件大小
    private int fileSize;
    //创建时间
    private String createdTime;
    //修改时间
    private String updatedTime;
    //已上传分片
    private int shardIndex;
    //分片总数
    private int shardTotal;
    //文件标识
    private String fileKey;
}