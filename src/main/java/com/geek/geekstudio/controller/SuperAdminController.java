package com.geek.geekstudio.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.geek.geekstudio.annotaion.SuperAdminPermission;
import com.geek.geekstudio.annotaion.UserLoginToken;
import com.geek.geekstudio.exception.NoUserException;
import com.geek.geekstudio.exception.ParameterError;
import com.geek.geekstudio.exception.PermissionDeniedException;
import com.geek.geekstudio.exception.UserRegisteredException;
import com.geek.geekstudio.model.dto.CourseDTO;
import com.geek.geekstudio.model.po.AdminPO;
import com.geek.geekstudio.model.vo.RestInfo;
import com.geek.geekstudio.service.proxy.SuperAdminServiceProxy;
import com.geek.geekstudio.service.proxy.CourseServiceProxy;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@Validated
@Data
@RequestMapping("/superAdmin")
public class SuperAdminController {
    @Autowired
    SuperAdminServiceProxy superAdminServiceProxy;
    @Autowired
    CourseServiceProxy courseServiceProxy;

    /**
     * 添加管理员 统一json格式   暂时AdminPO封装
     */
    @UserLoginToken
    @SuperAdminPermission
    @PostMapping("/addAdmin")
    public RestInfo addAdmin(@RequestBody @Validated AdminPO adminPO) throws UserRegisteredException {
        return superAdminServiceProxy.addAdmin(adminPO);
    }

    /**
     * 删除一个管理员
     */
    @UserLoginToken
    @SuperAdminPermission
    @PostMapping("/delAdmin")
    public RestInfo delAdmin(@RequestBody AdminPO adminPO) throws NoUserException {
        return superAdminServiceProxy.delAdmin(adminPO.getAdminId());
    }

    /**
     * 更新一个管理员信息
     */
    @UserLoginToken
    @SuperAdminPermission
    @PostMapping("/updateAdmin")
    public RestInfo updateAdmin(@RequestBody @Validated AdminPO adminPO) throws UserRegisteredException, PermissionDeniedException {
        return superAdminServiceProxy.updateAdmin(adminPO);
    }

    /**
     * json字符串中，如果value为""的话，后端对应属性如果是String类型的，那么接受到的就是""，如果是后端属性的类型是Integer、Double等类型，那么接收到的就是null。
     * json字符串中，如果value为null的话，后端对应收到的就是null
     * 删除多个管理员  接收多个字符串，
     * 后端接收json格式数组
     * 1.如果这个数组的元组是对象，则可以封装成List<对象类型>封装
     * 2.封装到一个普通的对象的集合属性、数组封装
     * 3.使用JSONObject对象接收，可以接收各种形式的，如普通的字符数组（但这样难以进行参数校验操作）
     */
    @UserLoginToken
    @SuperAdminPermission
    @PostMapping("/delAdmins")
    public RestInfo delAdmin(@RequestBody JSONObject jsonObject) throws NoUserException {
        JSONArray json=jsonObject.getJSONArray("userIdList");
        List<Object> idList=json.toJavaList(Object.class);
        return superAdminServiceProxy.delAdmins(idList);
    }

    /**
     * 查询管理员
     * 对于@RequestParam属性required标注true的注解，若未url中没传入此参数则程序会直接中断。
     * 对于@RequestParam属性required标注false(如果该参数也没有设置默认值)，
     * 或未用@RequestParam标注的参数在url中没有出现，如果该该参数用引用类型接收（String、Integer）则的值为null
     * 如果该参数用基本类型接收（int类型则值为0）
     */
    @UserLoginToken
    @SuperAdminPermission
    @GetMapping("/queryAdmins")
    public RestInfo queryAdmins(@RequestParam(name = "page",defaultValue = "1") int page,
                                @RequestParam(name = "rows",required = false,defaultValue = "10")int rows,
                                @RequestParam(name = "courseName",required = false) String courseName,
                                @RequestParam(name = "adminName",required = false) String adminName,
                                @RequestParam(name = "adminId",required = false) String adminId){
        return superAdminServiceProxy.queryAdmins(page,rows,courseName,adminName,adminId);
    }

    /**
     * 添加课程
     * 涉及int类型的参数建议使用Integer包装类
     */
    @UserLoginToken
    @SuperAdminPermission
    @PostMapping("/addCourse")
    public RestInfo addCourse(@RequestBody @Validated CourseDTO courseDTO) throws ParameterError {
        return courseServiceProxy.addCourse(courseDTO.getCourseId(),courseDTO.getCourseName());
    }

    /**
     *删除一个课程
     */
    @UserLoginToken
    @SuperAdminPermission
    @GetMapping("/delCourse")
    public RestInfo delCourse(@NotNull(message = "课程ID不为空")  int courseId) throws ParameterError{
        return courseServiceProxy.delCourse(courseId);
    }


    /*
    删除多个课程
    @UserLoginToken
    @SuperAdminPermission
    @PostMapping("/delCourses")
    public RestInfo delCourses(@NonNull @RequestParam("courseIdList") int[] courseIdList){
        return ;
    }*/
}
