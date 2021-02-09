package com.geek.geekstudio.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 封装返回前端的AdminBean no use now
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminVO {
    //属性待定
    private int id;
    private String adminId;
    private String adminName;
    private String password;
    private String courseName;
    private String image;
    private String registerTime;
    private String token;
    private String type;
}
