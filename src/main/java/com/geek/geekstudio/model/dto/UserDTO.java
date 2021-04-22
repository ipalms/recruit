package com.geek.geekstudio.model.dto;

import com.geek.geekstudio.group.UserGroupValidated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
  *用户类，主要用于封装传来前端数据  也可三层之间传递数据对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    //@NonNull
    private int id;
    @NotBlank(message = "学号不能为空！",groups = {UserGroupValidated.RegisterValidated.class,UserGroupValidated.SendMailValidated.class,UserGroupValidated.LoginValidated.class})
    @Length(min = 5,max = 30,groups = {UserGroupValidated.RegisterValidated.class,UserGroupValidated.SendMailValidated.class,UserGroupValidated.LoginValidated.class})
    private String userId;
    @NotBlank(message = "用户名不能为空！",groups = {UserGroupValidated.RegisterValidated.class})
    private String userName;
    @Email(message = "邮箱格式错误",groups ={UserGroupValidated.RegisterValidated.class,UserGroupValidated.SendMailValidated.class})
    @NotBlank(message = "邮箱不能为空！",groups ={UserGroupValidated.RegisterValidated.class,UserGroupValidated.SendMailValidated.class})
    private String mail;
    @NotBlank(message = "密码不能为空！",groups ={UserGroupValidated.RegisterValidated.class,UserGroupValidated.LoginValidated.class})
    @Length(min = 3,max = 30,groups ={UserGroupValidated.RegisterValidated.class,UserGroupValidated.LoginValidated.class})
    private String password;
    @NotBlank(message = "专业不能为空！",groups = {UserGroupValidated.RegisterValidated.class})
    private String major;
    private String sex;
    private String image;
    private String introduce;
    private String grade;
    private String registerTime;
    private Integer receiveMail;
    //较po没有的属性
    @NotBlank(message ="验证码不为空",groups ={UserGroupValidated.RegisterValidated.class})
    private String activeCode;
    private String newPassword;
    private String refreshToken;
    private Integer codeType;
    private Integer firstLogin;
}
