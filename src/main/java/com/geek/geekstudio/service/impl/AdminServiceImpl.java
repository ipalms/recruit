package com.geek.geekstudio.service.impl;

import com.geek.geekstudio.exception.ExceptionCode;
import com.geek.geekstudio.exception.ParameterError;
import com.geek.geekstudio.mapper.AdminMapper;
import com.geek.geekstudio.mapper.TaskMapper;
import com.geek.geekstudio.mapper.UserMapper;
import com.geek.geekstudio.mapper.WorkMapper;
import com.geek.geekstudio.model.dto.DailyMailDTO;
import com.geek.geekstudio.model.dto.MessageDTO;
import com.geek.geekstudio.model.po.TaskPO;
import com.geek.geekstudio.model.vo.*;
import com.geek.geekstudio.service.AdminService;
import com.geek.geekstudio.util.FileUtil;
import com.geek.geekstudio.websocket.service.UserSessionImpl;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.File;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.*;

@Service
@Slf4j
public class AdminServiceImpl implements AdminService {

    @Autowired
    UserSessionImpl userSession;

    @Autowired
    UserMapper userMapper;

    @Autowired
    TaskMapper taskMapper;

    @Autowired
    WorkMapper workMapper;

    @Autowired
    JavaMailServiceImpl javaMailService;

    @Autowired
    FileUtil fileUtil;

    /**
     *报名人数（包括前端、后端等具体方向）
     */
    @Override
    public RestInfo countAllUser(int courseId) {
        int count = userMapper.countDetailUser(courseId);
        return RestInfo.success("成功获取到人数！",count);
    }

    @Override
    public RestInfo sendDailyMail(DailyMailDTO dailyMailDTO) {
        //收集异常
        List<ErrorMsg> errorList = new ArrayList<>();
        Map<String,Future<String>> futureList=new HashMap<>();
        //局部变量如果在使用前一定会被赋值则可以不初始化为null，否则要初始化为null或给定一个默认值
        List<String> mails;
        List<String> userIdList;
        String mail;
        log.info("courseId:"+dailyMailDTO.getCourseId());
        if(dailyMailDTO.getUserIdList()==null){
            mails=userMapper.queryMails(dailyMailDTO.getCourseId());
        }else {
            //向给定的几个用户id发邮件
            mails=new ArrayList<>();
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
                errorList.add(new ErrorMsg(ExceptionCode.EMAIL_SEND_WRONG, "此次通知 邮箱号为:"+oneMail+" 的邮箱发送通知失败!", e.getMessage()));
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

    /**
     * 查询大一学生信息集合（可以选择方向）
     */
    @Override
    public RestInfo queryUsersInfo(int page, int rows, int courseId) {
        int total=userMapper.countDetailUser(courseId);
        int start=(page-1)*rows;
        //这个查询write了两种sql形式，一种xml一种注解形式
        List<UserVO> usersInfo=userMapper.queryUsersInfo(courseId,start,rows);
        int totalPage=total%rows==0?total/rows:total/rows+1;
        return RestInfo.success(new PageListVO(total,page,totalPage,rows,usersInfo));
    }

    /**
     * 查看某一方向学生的平均成绩
     */
    @Override
    public RestInfo queryScores(int courseId) {
        List<UserInfo> usersInfo=userMapper.queryAllUsersInfo(courseId);
        //task的记录
        List<TaskVO> taskVOList=taskMapper.queryTasksByCourseId(courseId);
        if(taskVOList==null){
            return RestInfo.success("该方向还没有一个作业，无数据！");
        }
        int totalScore=0;
        double avgScore,userScore=0.0;
        for(TaskVO taskVO:taskVOList){
            totalScore+=taskVO.getWeight()*10.0;
        }
        Map <String,Object> data=new HashMap<>();
        if(usersInfo!=null) {
            for (UserInfo user : usersInfo) {
                List<WorkVO> workVOList = workMapper.queryWorksByIdS(courseId, user.getUserId());
                if(workVOList!=null){
                    for(WorkVO workVO:workVOList){
                        //作业未打分视为10分
                        if(workVO.getScore()==null){
                            workVO.setScore(10);
                        }
                        userScore+=workVO.getScore()*workVO.getWeight();
                    }
                    //取小数点两位
                    avgScore=(double) Math.round(((userScore/totalScore))*1000)/100;
                    user.setAvgScore(avgScore);
                    user.setSubmitCount(workVOList.size());
                    userScore=0;
                }
            }
        }
        //按从大到小排序
        Collections.sort(usersInfo);
        data.put("total",taskVOList.size());
        data.put("users",usersInfo);
        return RestInfo.success(data);
    }

    /**
     * 统计提交作业和没提交作业的人数
     */
    @Override
    public RestInfo countStudent(int taskId) throws ParameterError {
        TaskPO taskPO = taskMapper.queryOneTaskById(taskId);
        if(taskPO==null){
            throw new ParameterError();
        }
        Map <String,Object> data=new HashMap<>();
        //报名总人数
        int total = userMapper.countDetailUser(taskPO.getCourseId());
        int submitPeople=workMapper.querySubmitTotal(taskId);
        int unSubmitPeople=total-submitPeople;
        data.put("submitPeople",submitPeople);
        data.put("unSubmitPeople",unSubmitPeople);
        return RestInfo.success("查询人数成功",data);
    }

    /**
     * 清理下载学生作业而产生的过程数据
     */
    @Override
    public RestInfo clearZipFile(int courseId) {
        String zipPath;
        if(courseId==0){
            //清除每个方向的过程垃圾
            zipPath="/zip";
        }else {
            zipPath="/zip/"+courseId;
        }
        fileUtil.deleteDir(new File(fileUtil.buildPath(zipPath)));
        return RestInfo.success("删除zip文件成功！");
    }

    /**
     * 开放接口可以向学员推送消息
     * 待定--可根据参数进行不同对象推送
     */
    @Override
    public RestInfo sendMessage(MessageDTO messageDTO) {
        userSession.sendMessage(messageDTO.getToId(),messageDTO.getFormId(),messageDTO.getWord());
        return RestInfo.success();
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
}
