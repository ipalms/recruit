package com.geek.geekstudio.model.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

/**
 *作业类，对应数据库task表  no use now
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
public class TaskPO {
    private int id;
    private int courseId;
    private String adminId;
    private String taskName;
    private String addTime;
    private String closeTime;
    private int commitLate;
    private int isClosed;
    private int weight;

    public TaskPO(int courseId, String adminId, String taskName, String addTime, String closeTime, int commitLate, int isClosed, int weight) {
        this.courseId = courseId;
        this.adminId = adminId;
        this.taskName = taskName;
        this.addTime = addTime;
        this.closeTime = closeTime;
        this.commitLate = commitLate;
        this.isClosed = isClosed;
        this.weight = weight;
    }
}
