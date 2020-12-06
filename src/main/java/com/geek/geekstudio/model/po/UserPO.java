package com.geek.geekstudio.model.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
  *PO用户类，对应数据库user表    -- 一般也用DO对应数据库层
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
    private String grade;
    private String registerTime;
}
