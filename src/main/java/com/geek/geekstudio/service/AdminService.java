package com.geek.geekstudio.service;


import com.geek.geekstudio.model.dto.DailyMailDTO;
import com.geek.geekstudio.model.vo.RestInfo;


public interface AdminService {

    //报名人数（包括前端、后端等具体方向）
    RestInfo countAllUser(Integer courseId);

    //发送日常邮件功能 （包括向选择具体方向的大一新生  或具体的用户[userIdList]）
    RestInfo sendDailyMail(DailyMailDTO dailyMailDTO) ;
}
