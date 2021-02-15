package com.geek.geekstudio.service.proxy;

import com.geek.geekstudio.exception.ParameterError;
import com.geek.geekstudio.model.dto.DailyMailDTO;
import com.geek.geekstudio.model.vo.RestInfo;
import com.geek.geekstudio.service.AdminService;
import com.geek.geekstudio.service.impl.AdminServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AdminServiceProxy implements AdminService {
    @Autowired
    AdminServiceImpl adminService;

    /**
     *报名人数（包括前端、后端等具体方向）
     */
    @Override
    public RestInfo countAllUser(int courseId) {
        return adminService.countAllUser(courseId);
    }

    /**
     * 发送日常邮件功能 （包括向选择具体方向的大一新生  或具体的用户[userIdList]）
     */
    @Override
    public RestInfo sendDailyMail(DailyMailDTO dailyMailDTO) {
        log.info("管理员："+dailyMailDTO.getAdminId()+"向大一同学推送了日常邮件");
        return adminService.sendDailyMail(dailyMailDTO);
    }

    /**
     * 查询大一学生信息集合（可以选择方向）
     */
    @Override
    public RestInfo queryUsersInfo(int page, int rows, int courseId) {
        return adminService.queryUsersInfo(page,rows,courseId);
    }

    /**
     * 查看某一方向学生的平均成绩
     */
    @Override
    public RestInfo queryScores(int courseId) {
        return adminService.queryScores(courseId);
    }

    /**
     * 统计提交作业和没提交作业的人数
     */
    @Override
    public RestInfo countStudent(int taskId) throws ParameterError {
        return adminService.countStudent(taskId);
    }

    /**
     * 清理下载学生作业而产生的过程数据
     */
    @Override
    public RestInfo clearZipFile(int courseId) {
        return adminService.clearZipFile(courseId);
    }
}
