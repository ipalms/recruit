package com.geek.geekstudio.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 封装返回前端的UserBean Object类型对象  no use now
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
    private String image;
    private String introduce;
    private String grade;
    private String registerTime;
    private String token;
    private String activeCode;
    private String receiveMail;
    //private String state;
}
