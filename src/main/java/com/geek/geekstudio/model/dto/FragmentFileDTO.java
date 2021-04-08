package com.geek.geekstudio.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FragmentFileDTO {
    //对应announce表的自增id
    private int id;
    //文件标识
    private String fileKey;

    private String fileName;

    private int shardSize;
}