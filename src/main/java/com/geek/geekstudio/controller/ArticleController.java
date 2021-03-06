package com.geek.geekstudio.controller;

import com.geek.geekstudio.annotaion.AdminPermission;
import com.geek.geekstudio.annotaion.PassToken;
import com.geek.geekstudio.annotaion.SuperAdminPermission;
import com.geek.geekstudio.annotaion.UserLoginToken;
import com.geek.geekstudio.exception.ParameterError;
import com.geek.geekstudio.exception.PermissionDeniedException;
import com.geek.geekstudio.exception.RecruitFileException;
import com.geek.geekstudio.model.dto.ArticleDTO;
import com.geek.geekstudio.model.vo.RestInfo;
import com.geek.geekstudio.service.proxy.ArticleServiceProxy;
import lombok.Data;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;

@RestController
@Validated
@Data
@RequestMapping("/article")
public class ArticleController {
    @Autowired
    ArticleServiceProxy articleServiceProxy;

    /**
     * 文章发布  --目前管理员发布文章
     * 1.普通文字+图片附件：对于图片附件的上传可在一次请求、也可分两次请求  articleTye=word
     * 2.可以直接上传markdown文件    articleTye=md
     * 对于CourseId用Integer类型接收，不传为null  而likeCount用int接收，不传为0
     */
    @UserLoginToken
    @AdminPermission
    @PostMapping("/addArticle")
    public RestInfo addArticle(@RequestBody ArticleDTO articleDTO){
        /*System.out.println("文章详情userId:"+articleDTO.getUserId()+",courseId:"+articleDTO.getCourseId()
                +",articleType:"+articleDTO.getArticleType()+",title:"+articleDTO.getTitle());*/
        return articleServiceProxy.addArticle(articleDTO);
    }

    /**
     *查询文章  可根据发布者名字或方向名查询
     */
    @PassToken
    @GetMapping("/queryArticles")
    public RestInfo queryArticles(@RequestParam(name = "page",defaultValue = "1",required=false) int page,
                                  @RequestParam(name = "rows",required = false,defaultValue = "10")int rows,
                                  @RequestParam(name = "adminName",required = false) String adminName,
                                  @RequestParam(name = "courseName",required = false) String courseName,
                                  @RequestParam(name = "userId",required = false) String userId){
        //System.out.println("courseName:"+courseName);
        return articleServiceProxy.queryArticles(page,rows,adminName,courseName,userId);
    }

    /**
     *查询一篇文章详细内容
     */
    @PassToken
    @GetMapping("/queryOneArticle")
    public RestInfo queryOneArticle(@RequestParam("articleId") int articleId,
                                    @RequestParam(name = "userId",required = false) String userId) throws RecruitFileException {
        return articleServiceProxy.queryOneArticle(articleId,userId);
    }


    /**
     * 查询自己发表的文章
     */
    @UserLoginToken
    @AdminPermission
    @GetMapping("/queryMyArticles")
    public RestInfo queryMyArticles(@RequestParam(name = "page",defaultValue = "1",required=false) int page,
                                    @RequestParam(name = "rows",required = false,defaultValue = "10")int rows,
                                    @RequestParam(name = "userId") String userId){
        return articleServiceProxy.queryMyArticles(page,rows,userId);
    }

    /**
     * 删除一篇发布的文章
     */
    @UserLoginToken
    @AdminPermission
    @PostMapping("/deleteArticle")
    public RestInfo deleteArticle(@RequestBody ArticleDTO articleDTO) throws ParameterError, PermissionDeniedException {
        return articleServiceProxy.deleteArticle(articleDTO);
    }
}
