package com.geek.geekstudio.model.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * 注册Bean 主要用于校验注册时User对象数据是否合规  (待用)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterPO {
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
