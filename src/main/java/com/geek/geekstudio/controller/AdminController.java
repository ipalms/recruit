package com.geek.geekstudio.controller;

import com.geek.geekstudio.annotaion.AdminPermission;
import com.geek.geekstudio.annotaion.UserLoginToken;
import com.geek.geekstudio.exception.ParameterError;
import com.geek.geekstudio.exception.RecruitFileException;
import com.geek.geekstudio.model.dto.DailyMailDTO;
import com.geek.geekstudio.model.dto.MessageDTO;
import com.geek.geekstudio.model.dto.UserDTO;
import com.geek.geekstudio.model.dto.WorkDTO;
import com.geek.geekstudio.model.vo.RestInfo;
import com.geek.geekstudio.service.AdminService;
import com.geek.geekstudio.service.proxy.AdminServiceProxy;
import com.geek.geekstudio.service.proxy.TaskServiceProxy;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@Validated
@Data
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    AdminServiceProxy adminServiceProxy;

    @Autowired
    TaskServiceProxy taskServiceProxy;

    /**
     *报名人数（包括前端、后端等具体方向）
     *参数int型要指定默认值，不然报错
     */
    @AdminPermission
    @UserLoginToken
    @GetMapping("/countAllUser")
    public RestInfo countAllUser(@RequestParam(value = "courseId",required = false,defaultValue = "0") int courseId){
        return adminServiceProxy.countAllUser(courseId);
    }

    /**
     * 发送日常邮件功能 （包括向选择具体方向的大一新生  或具体的用户[userIdList]）
     */
    @AdminPermission
    @UserLoginToken
    @PostMapping("/sendDailyMail")
    public RestInfo sendDailyMail(@RequestBody DailyMailDTO dailyMailDTO){
        return adminServiceProxy.sendDailyMail(dailyMailDTO);
    }

    /**
     * 查询大一学生信息集合（可以选择方向）
     */
    @AdminPermission
    @UserLoginToken
    @GetMapping("/queryUsersInfo")
    public RestInfo queryUsersInfo(@RequestParam(name = "page",defaultValue = "1",required=false) int page,
                                   @RequestParam(name = "rows",required = false,defaultValue = "10")int rows,
                                   @RequestParam(value = "courseId",required = false,defaultValue = "0") int courseId){
        return adminServiceProxy.queryUsersInfo(page,rows,courseId);
    }

    /**
     *管理员查看自己发布的作业
     */
    @AdminPermission
    @UserLoginToken
    @GetMapping("/queryMyTasks")
    public RestInfo queryMyTasks(@RequestParam(name = "adminId") String adminId,
                                 HttpServletRequest request)  {
        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
        return taskServiceProxy.queryMyTasks(adminId,baseUrl);
    }

    /**
     *管理员查看某项作业提交作业名单及详细数据（分页）
     */
    @AdminPermission
    @UserLoginToken
    @GetMapping("/queryOneTask")
    public RestInfo queryOneTask(@RequestParam(name = "page",defaultValue = "1",required=false) int page,
                                 @RequestParam(name = "rows",defaultValue = "10",required = false)int rows,
                                 @RequestParam(name = "taskId") int taskId,
                                 HttpServletRequest request)  {
        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
        return taskServiceProxy.queryOneTask(page,rows,taskId,baseUrl);
    }

    /**
     *给某个作业打分（分值1~10）
     * 参数1.id 2.score
     * 不传score就是取消已打分数
     */
    @AdminPermission
    @UserLoginToken
    @PostMapping("/giveScore")
    public RestInfo giveScore(@RequestBody WorkDTO workDTO) throws ParameterError{
        return taskServiceProxy.giveScore(workDTO);
    }

    /** request.getScheme();//http
        request.getServerName();//localhost
        request.getServerPort();//8080
        request.getContextPath();//项目名
        String url = scheme+"://"+serverName+":"+serverPort+contextPath;//http://127.0.0.1:8080/test*/
    /**
     * 批下载一个任务对应的作业
     * 参数：1.taskId 2.List<String> userIdList
     * 如果没传第二个参数就下载已提交的所有作业
     */
    @AdminPermission
    @UserLoginToken
    @PostMapping("downloadWorks")
    public RestInfo downloadWorks(@RequestBody WorkDTO workDTO,
                                  HttpServletRequest request) throws ParameterError, RecruitFileException {
        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
        return taskServiceProxy.downloadWorks(workDTO,baseUrl);
    }

    /**
     * 查看某一方向学生的平均成绩和提交作业的次数
     */
    @AdminPermission
    @UserLoginToken
    @GetMapping("queryScores")
    public RestInfo queryGrades(@RequestParam(name = "courseId") int courseId) {
        return adminServiceProxy.queryScores(courseId);
    }

    /**
     * 统计提交作业和没提交作业的人数
     */
    @AdminPermission
    @UserLoginToken
    @GetMapping("countStudent")
    public RestInfo countStudent(@RequestParam(name = "taskId") int taskId) throws ParameterError {
        return adminServiceProxy.countStudent(taskId);
    }

    /**
     * 清理下载学生作业而产生的过程数据（可传入courseId参数）
     */
    @AdminPermission
    @UserLoginToken
    @GetMapping("clearZipFile")
    public RestInfo clearZipFile(@RequestParam(name = "courseId",required = false,defaultValue = "0") int courseId) {
        return adminServiceProxy.clearZipFile(courseId);
    }

    /**
     * 开放接口可以向学员推送消息
     */
    @AdminPermission
    @UserLoginToken
    @PostMapping("/sendMessage")
    public RestInfo sendMessage(@RequestBody MessageDTO messageDTO){
        return adminServiceProxy.sendMessage(messageDTO);
    }
}
