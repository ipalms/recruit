package com.geek.geekstudio.service.impl;

import com.geek.geekstudio.exception.*;
import com.geek.geekstudio.mapper.SuperAdminMapper;
import com.geek.geekstudio.mapper.CourseMapper;
import com.geek.geekstudio.model.po.AdminPO;
import com.geek.geekstudio.model.vo.ErrorMsg;
import com.geek.geekstudio.model.vo.PageListVO;
import com.geek.geekstudio.model.vo.RestInfo;
import com.geek.geekstudio.service.SuperAdminService;
import com.geek.geekstudio.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class SuperAdminServiceImpl implements SuperAdminService {

    @Autowired
    SuperAdminMapper superAdminMapper;
    @Autowired
    CourseMapper courseMapper;

    private static final String defaultPicture="/image/default.jpg";

    /**
     * 添加管理员
     */
    @Override
    public RestInfo addAdmin(AdminPO adminPO) throws UserRegisteredException {
        if(superAdminMapper.queryAdminByAdminId(adminPO.getAdminId())!=null){
            throw new UserRegisteredException("此管理员ID已被注册");
        }
        adminPO.setRegisterTime(DateUtil.creatDate());
        adminPO.setType("admin");
        adminPO.setImage(defaultPicture);
        superAdminMapper.addAdmin(adminPO);
        return RestInfo.success("管理员添加成功！",null);
    }

    /**
     * 删除管理员
     */
    //删除失败回滚
    @Transactional(rollbackFor = Exception.class)
    @Override
    public RestInfo delAdmin(String userId) throws NoUserException {
        AdminPO adminPO=superAdminMapper.queryAdminByAdminId(userId);
        if(adminPO==null){
            throw new NoUserException("无此用户");
        }
        //此处应该删除跟管理员相关的一切记录 -->发送的文章、公告等等  待续
        superAdminMapper.deleteAdminByAdminId(userId);
        return RestInfo.success("管理员删除成功！",null);
    }

    @Override
    public RestInfo delAdmins(List<Object> userIdList) {
        List<ErrorMsg> errorList = new ArrayList<>();
        for(Object id:userIdList){
            try {
                //删除单个用户可能会有异常
                delAdmin((String) id);
            }catch (Exception e) {
                //收集每个可能产生的异常
                errorList.add(new ErrorMsg(ExceptionCode.NO_USER, "管理员"+id+"删除失败!", e.getMessage()));
            }
        }
        return RestInfo.success("一共删除"+userIdList.size()+"个管理员,成功删除"+(userIdList.size()-errorList.size())+"个",errorList);
    }

    /**
     * 更新一个管理员信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestInfo updateAdmin(AdminPO adminPO) throws UserRegisteredException, PermissionDeniedException {
        if(superAdminMapper.queryAdminByAdminId(adminPO.getAdminId())!=null){
            throw new UserRegisteredException();
        }
        if("super".equals(adminPO.getType())){
            throw new PermissionDeniedException("不能更改到super权限");
        }
        superAdminMapper.updateAdmin(adminPO);
        return RestInfo.success("修改管理员信息成功！",null);
    }

    /**
     * 查询管理员
     */
    @Override
    public RestInfo queryAdmins(int page, int rows, String courseName, String adminName, String adminId) {
        int total=superAdminMapper.queryAdminTotal(courseName,adminName,adminId);
        int start=(page-1)*rows;
        List<AdminPO> adminPOList=superAdminMapper.queryAdmins(courseName,adminName,adminId,start,rows);
        int totalPage=total%rows==0?total/rows:total/rows+1;
        return RestInfo.success(new PageListVO(total,page,totalPage,rows,adminPOList));
    }
}
