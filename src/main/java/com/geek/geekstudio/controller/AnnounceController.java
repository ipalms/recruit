package com.geek.geekstudio.controller;

import com.geek.geekstudio.annotaion.AdminPermission;
import com.geek.geekstudio.annotaion.PassToken;
import com.geek.geekstudio.annotaion.UserLoginToken;
import com.geek.geekstudio.exception.ParameterError;
import com.geek.geekstudio.exception.RecruitException;
import com.geek.geekstudio.model.dto.AnnounceDTO;
import com.geek.geekstudio.model.vo.RestInfo;
import com.geek.geekstudio.service.proxy.AdminServiceProxy;
import com.geek.geekstudio.service.proxy.AnnounceServiceProxy;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@RestController
@Validated
@Data
@Slf4j
@RequestMapping("/announce")
public class AnnounceController {
    @Autowired
    AnnounceServiceProxy announceServiceProxy;

    /**
     *发布公告 multipart/form-data
     */
    @AdminPermission
    @UserLoginToken
    @PostMapping("/addAnnounce")
    public RestInfo addAnnounce(@RequestParam(name = "adminId") @NotBlank(message = "管理员id不能为空")String adminId,
                                @RequestParam(name = "courseId",required = false) Integer courseId,
                                @RequestParam(name = "title") @Size(min = 1,max=60)String title,
                                @RequestParam(name = "content") String content,
                                @RequestParam(name = "file", required = false)MultipartFile file) throws RecruitException {
        return announceServiceProxy.addAnnounce(adminId,courseId,title,content,file);
    }

    /**
     * 删除公告
     */
    @AdminPermission
    @UserLoginToken
    @GetMapping("/delAnnounce/{id}")
    public RestInfo delAnnounce(@PathVariable(name = "id") int id) throws RecruitException {
        return announceServiceProxy.delAnnounce(id);
    }

    /**
     * 查看发布的公告
     */
    @PassToken
    @GetMapping("/queryAnnounce")
    public RestInfo queryAnnounce(@RequestParam(name = "page",defaultValue = "1",required=false) int page,
                                  @RequestParam(name = "rows",defaultValue = "6",required = false)int rows,
                                  @RequestParam(name = "courseId",defaultValue = "0",required = false) int courseId){
        return announceServiceProxy.queryAnnounce(page,rows,courseId);
    }

    /**
     * 查看一条公告的详情
     */
    @PassToken
    @GetMapping("/queryOneAnnounce/{id}")
    public RestInfo queryOneAnnounce(@PathVariable(name = "id") int id,HttpServletRequest request) throws ParameterError {
        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
        return announceServiceProxy.queryOneAnnounce(id,baseUrl);
    }
}
