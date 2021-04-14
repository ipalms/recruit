package com.geek.geekstudio.service.impl;

import com.geek.geekstudio.exception.ParameterError;
import com.geek.geekstudio.exception.RecruitException;
import com.geek.geekstudio.mapper.AnnounceMapper;
import com.geek.geekstudio.model.po.AnnouncePO;
import com.geek.geekstudio.model.vo.AnnounceVO;
import com.geek.geekstudio.model.vo.ArticleInfoVO;
import com.geek.geekstudio.model.vo.PageListVO;
import com.geek.geekstudio.model.vo.RestInfo;
import com.geek.geekstudio.service.AnnounceService;
import com.geek.geekstudio.util.DateUtil;
import com.geek.geekstudio.util.FileUtil;
import com.geek.geekstudio.websocket.service.UserSessionImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Slf4j
public class AnnounceServiceImpl implements AnnounceService {

    @Autowired
    AnnounceMapper announceMapper;

    @Autowired
    FileUtil fileUtil;

    @Autowired
    UserSessionImpl userSession;

    /** 注入缓存 */
    @Autowired
    RedisTemplate<Object,Object> redisTemplate;

    /**
     *发布公告 还未限制文件大小，默认1G
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public RestInfo addAnnounce(String adminId, Integer courseId, String title, String content, MultipartFile file) throws RecruitException {
        String fileName=null,filePath,url=null;
        if(file!=null){
            fileName = file.getOriginalFilename();
            filePath = fileUtil.buildAnnounceFilePath(courseId);
            url=fileUtil.storeFile(filePath,file,fileName);
        }
        AnnouncePO announcePO=new AnnouncePO(courseId,adminId,title,content,DateUtil.creatDate(),fileName,url);
        announceMapper.addAnnounce(announcePO);
        redisTemplate.opsForHash().put("announce",announcePO.getId()+"",announcePO);
        /*if(courseId!=null){
            String dir = userSession.courseRelation.get(courseId);
            userSession.sendMessage(dir,"系统","您所选"+dir+"的方向有一条新公告");
        }else {
            userSession.sendMessage("all","系统","有一条新公告");
        }*/
        return RestInfo.success("推送公告成功",announcePO.getId());
    }

    /**
     * 删除公告
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public RestInfo delAnnounce(int id) throws RecruitException {
        AnnouncePO announcePO=announceMapper.findAnnounceById(id);
        if(announcePO==null){
            throw new ParameterError("id为空，请重新选定待删除公告！");
        }
        if(announcePO.getFileName()!=null){
            fileUtil.deleteFile(fileUtil.buildPath(announcePO.getFilePath()));
        }
        announceMapper.delAnnounce(id);
        redisTemplate.opsForHash().delete("announce",announcePO.getId()+"");
        return RestInfo.success("删除公告成功",null);
    }

    /**
     * 查看发布的公告
     */
    @Override
    public RestInfo queryAnnounce(int page, int rows, int courseId) {
        int total=announceMapper.queryAnnounceTotal(courseId);
        int start=(page-1)*rows;
        List<AnnounceVO> announceVOList=announceMapper.queryAnnounceList(courseId,start,rows);
        int totalPage=total%rows==0?total/rows:total/rows+1;
        return RestInfo.success(new PageListVO(total,page,totalPage,rows,announceVOList));
    }

    /**
     * 查看发布的公告
     */
    @Override
    public RestInfo queryOneAnnounce(int id) throws ParameterError {
        AnnouncePO announcePO= (AnnouncePO) redisTemplate.opsForHash().get("announce",id+"");
        if(announcePO==null){
            announcePO=announceMapper.findAnnounceById(id);
            if(announcePO==null){
                throw new ParameterError();
            }
            redisTemplate.opsForHash().put("announce",announcePO.getId()+"",announcePO);
        }
        if(announcePO.getFileName()!=null){
            //改成直接路径
            announcePO.setFilePath(fileUtil.getFileUrl(announcePO.getFilePath()));
        }
        return RestInfo.success("公告详情",announcePO);
    }
}
