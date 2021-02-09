package com.geek.geekstudio.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 *文件类，接收前端参数
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskDTO {
    private int id;
    private int courseId;
    private String adminId;
    private String taskName;
    private String addTime;
    private long effectiveTime;//有效期截至的时间戳
    private int commitLate;//是否可以超时提交 0不能，1能
    private int isClosed;//作业是否关闭提交通道 0否，1是
    @Max(10)
    @Min(1)
    private int weight=1;
}
