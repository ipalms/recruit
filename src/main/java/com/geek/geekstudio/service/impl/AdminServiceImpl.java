package com.geek.geekstudio.service.impl;

import com.geek.geekstudio.exception.ExceptionCode;
import com.geek.geekstudio.mapper.AdminMapper;
import com.geek.geekstudio.mapper.UserMapper;
import com.geek.geekstudio.model.dto.DailyMailDTO;
import com.geek.geekstudio.model.vo.ErrorMsg;
import com.geek.geekstudio.model.vo.RestInfo;
import com.geek.geekstudio.service.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class AdminServiceImpl implements AdminService {
    @Autowired
    AdminMapper adminMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    JavaMailServiceImpl javaMailService;

    /**
     *报名人数（包括前端、后端等具体方向）
     */
    @Override
    public RestInfo countAllUser(Integer courseId) {
        int count;
        if(courseId==null){
            count=adminMapper.countAllUser();
        }else {
            count = adminMapper.countDetailUser(courseId);
        }
        return RestInfo.success("成功获取到人数！",count);
    }

    /**
     * 发送日常邮件功能 （包括向选择具体方向的大一新生  或具体的用户[userIdList]）
     */
    @Override
    public RestInfo sendDailyMail(DailyMailDTO dailyMailDTO) {
        //收集异常
        List<ErrorMsg> errorList = new ArrayList<>();
        List<String> mails=null;
        List<String> userIdList=null;
        String mail=null;
        log.info("courseId:"+dailyMailDTO.getCourseId());
        if(dailyMailDTO.getUserIdList()==null){
            mails=userMapper.queryMails(dailyMailDTO.getCourseId());
        }else {
            //向给定的几个用户id发邮件
            mails=new ArrayList<String>();
            userIdList=dailyMailDTO.getUserIdList();
            for(String userId:userIdList){
                mail=userMapper.queryMailByUserId(userId);
                //该用户可能点击不再接收日常邮件，所以要判空。或userId有误也回导致mail为空
                if(mail!=null){
                    mails.add(mail);
                }
            }
        }
        //发送邮件
        for(String oneMail:mails){
            try {
                javaMailService.sendDailyMail(oneMail,dailyMailDTO.getTitle(),dailyMailDTO.getText());
            } catch (Exception e) {
                errorList.add(new ErrorMsg(ExceptionCode.EMAIL_SEND_WRONG, "此次通知 邮箱号为:"+oneMail+" 的邮箱发送通知失败!", e.getMessage()));
            }
        }
        return RestInfo.success("一共发送通知给"+mails.size()+"个邮箱,通知推送成功的有"+(mails.size()-errorList.size())+"个",errorList);
    }
}
