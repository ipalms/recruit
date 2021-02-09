package com.geek.geekstudio.service.impl;

import com.geek.geekstudio.exception.ExceptionCode;
import com.geek.geekstudio.mapper.AdminMapper;
import com.geek.geekstudio.mapper.UserMapper;
import com.geek.geekstudio.model.dto.DailyMailDTO;
import com.geek.geekstudio.model.vo.ErrorMsg;
import com.geek.geekstudio.model.vo.RestInfo;
import com.geek.geekstudio.service.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

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
    /*@Override
    public RestInfo sendDailyMail(DailyMailDTO dailyMailDTO) {
        //收集异常
        List<ErrorMsg> errorList = new ArrayList<>();
        Map<String,Future<String>> futureList=new HashMap<>();
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
        //发送邮件，使用异步后可能不能准确的统计出异常，因为使用的线程池发送消息而主线程可能已经结束返回消息了
        //对于有返回值的异步任务，使用Future的get方法能捕获到异步任务出现的异常
        CountDownLatch可以等到所有任务都结束了主线程才继续运行
        CountDownLatch latch = new CountDownLatch(mails.size());
        for(String oneMail:mails){
            try {
                Future<String> future = javaMailService.sendDailyMail(oneMail, dailyMailDTO.getTitle(), dailyMailDTO.getText(), latch);
                futureList.put(oneMail,future);
            } catch (Exception e) {
                //这里可以不用处理
                errorList.add(new ErrorMsg(ExceptionCode.EMAIL_SEND_WRONG, "此次通知 邮箱号为:"+oneMail+" 的邮箱发送通知失败!", e.getMessage()));
            }
        }
        for(Map.Entry<String, Future<String>> entry: futureList.entrySet()){
            try {
                entry.getValue().get(20, TimeUnit.SECONDS);
                //future.get(20, TimeUnit.SECONDS);
            } catch (Exception e) {
                errorList.add(new ErrorMsg(ExceptionCode.EMAIL_SEND_WRONG, "此次通知 邮箱号为:"+entry.getKey()+" 的邮箱发送通知失败!", e.getMessage()));
            }
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return RestInfo.success("一共发送通知给"+mails.size()+"个邮箱,通知推送成功的有"+(mails.size()-errorList.size())+"个",errorList);
    }*/


    @Override
    public RestInfo sendDailyMail(DailyMailDTO dailyMailDTO) {
        //收集异常
        List<ErrorMsg> errorList = new ArrayList<>();
        Map<String,Future<String>> futureList=new HashMap<>();
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
        //发送邮件，使用异步后可能不能准确的统计出异常，因为使用的线程池发送消息而主线程可能已经结束返回消息了
        //对于有返回值的异步任务，使用Future的get方法能获得异步任务的返回值以及捕获到异步任务出现的异常
        //CountDownLatch可以等到所有任务都结束了主线程才继续运行
        //CountDownLatch latch = new CountDownLatch(mails.size());
        for(String oneMail:mails){
            try {
                futureList.put(oneMail,javaMailService.sendDailyMail(oneMail, dailyMailDTO.getTitle(), dailyMailDTO.getText()));
            } catch (Exception e) {
                //这里可以不用处理
                //errorList.add(new ErrorMsg(ExceptionCode.EMAIL_SEND_WRONG, "此次通知 邮箱号为:"+oneMail+" 的邮箱发送通知失败!", e.getMessage()));
            }
        }
        //捕获可能出现的异常(另一种方案引入guava的并发包中的回调方法)
        for(Map.Entry<String, Future<String>> entry: futureList.entrySet()){
            try {
                //阻塞式获取异步任务返回值 带参数要求在指定时间内获得返回值
                entry.getValue().get(20, TimeUnit.SECONDS);
            } catch (Exception e) {
                errorList.add(new ErrorMsg(ExceptionCode.EMAIL_SEND_WRONG, "此次通知 邮箱号为:"+entry.getKey()+" 的邮箱发送通知失败!", e.getMessage()));
            }
        }
        return RestInfo.success("一共发送通知给"+mails.size()+"个邮箱,通知推送成功的有"+(mails.size()-errorList.size())+"个",errorList);
    }
}
