package com.geek.geekstudio.model.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 *管理员类，对应数据库admin表
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminPO {
    private int id;
/*    @NotBlank(message = "唯一标识不能为空!")
    @Length(min = 5,max = 30)*/
    private String adminId;
/*    @NotBlank(message = "姓名不能为空！")*/
    private String adminName;
/*    @NotBlank(message = "密码不能为空！")
    @Length(min = 3,max = 30)*/
    private String password;
    private String courseName;
    private String image;
    private String registerTime;
    private String type;
}
