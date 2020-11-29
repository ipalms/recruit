package com.geek.geekstudio.model.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
  *用户类，对应数据库user表
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserPO {
    private int id;
    private String userId;
    private String userName;
    private String mail;
    private String password;
    private String major;
    private String sex;
    private String securityQuestion;
    private String answer;
    private String image;
    private String introduce;
}
