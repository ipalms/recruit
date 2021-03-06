package com.geek.geekstudio.model.vo;

import com.geek.geekstudio.model.po.WorkFilePO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 封装返回前端的WorkBean
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkVO {
    private int id;
    private int taskId;
    private int courseId;
    private String userId;
    private UserVO userVO;
    private String addTime;
    private Integer score;
    private Integer weight;
    private String filePath;
    private List<WorkFileVO> workFileVOList;
}
