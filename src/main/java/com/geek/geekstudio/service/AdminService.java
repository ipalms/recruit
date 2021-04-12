package com.geek.geekstudio.service;


import com.geek.geekstudio.exception.ParameterError;
import com.geek.geekstudio.model.dto.DailyMailDTO;
import com.geek.geekstudio.model.dto.MessageDTO;
import com.geek.geekstudio.model.vo.RestInfo;


public interface AdminService {

    //报名人数（包括前端、后端等具体方向）
    RestInfo countAllUser(int courseId);

    //发送日常邮件功能 （包括向选择具体方向的大一新生  或具体的用户[userIdList]）
    RestInfo sendDailyMail(DailyMailDTO dailyMailDTO) ;

    //查询大一学生信息集合
    RestInfo queryUsersInfo(int page, int rows, int courseId);

    //查询某个方向的平均成绩
    RestInfo queryScores(int courseId);

    //统计提交作业和没提交作业的人数
    RestInfo countStudent(int taskId) throws ParameterError;

    //清理下载学生作业而产生的过程数据
    RestInfo clearZipFile(int courseId);

    //开放接口可以向学员推送消息
    RestInfo sendMessage(MessageDTO messageDTO);
}
