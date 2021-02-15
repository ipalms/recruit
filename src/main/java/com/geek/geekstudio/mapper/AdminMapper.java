package com.geek.geekstudio.mapper;

import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminMapper {

    /*//总报名人数
    @Select("SELECT COUNT(DISTINCT userId) FROM direction")
    int countAllUser();*/

    //更新头像url
    @Update("UPDATE admin SET image=#{image} WHERE adminId=#{adminId}")
    void updateAdminImage(String adminId, String image);

    //查询管理员的类别
    @Select("SELECT type FROM admin WHERE adminId=#{adminId}")
    String queryTypeById(String adminId);
}
