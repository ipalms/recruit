package com.geek.geekstudio.model.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *管理员类，对应数据库admin表
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminPO {
    private int id;
    private String adminId;
    private String adminName;
    private String password;
    private String direction;
    private String image;
}
