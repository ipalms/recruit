package com.geek.geekstudio.model.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
  *用户类，对应数据库user表
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserPO {
    //参数校验有问题
    //@NonNull
    private int id;
    @NotBlank(message = "学号不能为空！")
    @Length(min = 5,max = 30)
    private String userId;
    @NotBlank(message = "姓名不能为空！")
    private String userName;
    @Email(message = "邮箱格式错误")
    @NotBlank(message = "邮箱不能为空！")
    private String mail;
    @NotBlank(message = "密码不能为空！")
    @Length(min = 3,max = 30)
    private String password;
    @NotBlank(message = "专业不能为空！")
    private String major;
    private String sex;
    private String securityQuestion;
    private String answer;
    private String image;
    private String introduce;
    private String grade;
    //private String state;
    private String registerTime;
    //private String activeCode;
}