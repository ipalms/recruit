package com.geek.geekstudio.model.po;

import com.geek.geekstudio.model.vo.DirectionVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
  *PO用户类，对应数据库user表    --  一般也用DO对应数据库层
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
    private String image;
    private String introduce;
    private String grade;
    private String registerTime;
    private String receiveMail;
    private List<DirectionVO> directionVOList;
}
