package com.geek.geekstudio.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 封装返回前端的UserBean
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserVO {

    //属性待定
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
