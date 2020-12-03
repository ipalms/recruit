package com.geek.geekstudio.mapper;

import com.geek.geekstudio.model.po.AdminPO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminMapper {
    /**
     *通过id 密码查管理员
     */
    @Select("SELECT * FROM admin WHERE adminId=#{adminId} AND password=#{password}")
    AdminPO queryAdminByUserIdAndPassword(String adminId, String password);

    /**
     *通过id查管理员
     */
    @Select("SELECT * FROM admin WHERE adminId=#{adminId}")
    AdminPO queryAdminByAdminId(String adminId);

    /**
     *添加管理员
     */
    @Insert("INSERT INTO admin (adminId,adminName,password,courseName,image,registerTime,type)" +
            " VALUES (#{adminId},#{adminName},#{password},#{courseName},#{image},#{registerTime},#{type})")
    void addAdmin(AdminPO adminPO);

    /**
     *删除管理员
     */
    @Delete("DELETE FROM admin WHERE adminId=#{adminId}")
    void deleteAdminByAdminId(String adminId);
}
