package com.geek.geekstudio.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 封装返回前端的UserBean Object类型对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo implements Comparable<UserInfo> {
    private String userId;
    private String userName;
    private String mail;
    private String major;
    private String image;
    private String grade;
    private int submitCount;
    private double avgScore;

    @Override
    public int compareTo(UserInfo o) {
        return (int)((o.avgScore-this.avgScore)*100);
    }
}
