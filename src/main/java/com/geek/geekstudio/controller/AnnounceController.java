package com.geek.geekstudio.controller;

import com.geek.geekstudio.annotaion.AdminPermission;
import com.geek.geekstudio.annotaion.PassToken;
import com.geek.geekstudio.annotaion.UserLoginToken;
import com.geek.geekstudio.exception.ParameterError;
import com.geek.geekstudio.exception.RecruitException;
import com.geek.geekstudio.exception.RecruitFileException;
import com.geek.geekstudio.model.dto.AnnounceDTO;
import com.geek.geekstudio.model.dto.FragmentFileDTO;
import com.geek.geekstudio.model.vo.RestInfo;
import com.geek.geekstudio.service.FileService;
import com.geek.geekstudio.service.proxy.AdminServiceProxy;
import com.geek.geekstudio.service.proxy.AnnounceServiceProxy;
import com.geek.geekstudio.service.proxy.FileServiceProxy;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.*;
import java.util.UUID;

@RestController
@Validated
@Data
@Slf4j
@RequestMapping("/announce")
public class AnnounceController {
    @Autowired
    AnnounceServiceProxy announceServiceProxy;

    @Autowired
    FileServiceProxy fileServiceProxy;

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

    /**
     * 总体完成了文件断点续传的功能
     * 其次这个demo续传的文件是按分片分成了数个文件，最后再做合并的【追加文件到末尾】。
     * 也可以采用RandomAccessFile类对同一个文件追加数据。
     */
    /**
     * announce文件上传（断点续传）
     * @param file  //代表这个文件的file对象
     * @param shardIndex   //当前要上传的分页
     * @param shardTotal  //文件分片的总数
     * @param fileSize  //文件的总大小
     * @param courseId  //文件的方向
     * @param fileKey  //文件的唯一标识（同一个文件的唯一标识相同--前端依据文件内容进行md5的hash计算）
     */
/*       @UserLoginToken
         @AdminPermission*/
    @PostMapping("/announceUpload")
    public RestInfo announceUpload(MultipartFile file,
                         int shardIndex,
                         int shardTotal,
                         Integer fileSize,
                         @RequestParam(defaultValue = "0",required = false) int courseId,
                         String fileKey) throws RecruitFileException {
        return fileServiceProxy.announceUpload(file, shardIndex, shardTotal, fileSize, courseId, fileKey);
    }

    /**
     * 检查数据库中有没有这个文件的存在(根据文件的唯一标识)  json
     * 如果没有前端就直接从第一个分页开始上传
     * 如果有的话，返回这个文件稳定的上传分页点（即前面断点的文件完整）
     * 如过是最后一页的话就说明已上传成功，否则就从当前断开的分页开始继续上传
     */
   /* @UserLoginToken
    @AdminPermission
    @PostMapping("/check")
    public RestInfo check(@RequestBody FragmentFileDTO fragmentFileDTO){
        return fileServiceProxy.check(fragmentFileDTO.getKey());
    }*/


    @PostMapping("/check")
    public RestInfo check(String fileKey ,int shardSize){
        return fileServiceProxy.check(fileKey,shardSize);
    }

    /**
     * 合并分页 json
     */
    /*    @UserLoginToken
    @AdminPermission*/
    @PostMapping("/merge")
    public RestInfo merge(@RequestBody FragmentFileDTO fragmentFileDTO) throws RecruitFileException, InterruptedException{
        return fileServiceProxy.merge(fragmentFileDTO.getFileKey(),fragmentFileDTO.getId(),fragmentFileDTO.getFileName());
    }
}
