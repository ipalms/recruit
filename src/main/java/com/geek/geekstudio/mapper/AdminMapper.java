package com.geek.geekstudio.mapper;

import com.geek.geekstudio.model.po.AdminPO;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

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

    /**
     *更新管理员信息
     */
    @Update("UPDATE admin SET adminId=#{adminId},adminName=#{adminName}," +
            "password=#{password},courseName=#{courseName},image=#{image} WHERE id=#{id}")
    void updateAdmin(AdminPO adminPO);

    /**
     *查询一定条件下的总管理员数
     */
    int queryAdminTotal(@Param("courseName") String courseName, @Param("adminName") String adminName,@Param("adminId") String adminId);

    /**
     *查询一定条件下某一页的用户信息
     */
    List<AdminPO> queryAdmins(String courseName, String adminName, String adminId, int start, int rows);
}
