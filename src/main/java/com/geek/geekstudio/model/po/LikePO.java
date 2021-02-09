package com.geek.geekstudio.model.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *管理员类，对应数据库like表  no use now
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LikePO {
    private int id;
    private int articleId;
    private String userId;
    private String likeTime;
}
