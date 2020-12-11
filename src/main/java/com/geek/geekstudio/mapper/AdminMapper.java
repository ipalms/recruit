package com.geek.geekstudio.mapper;

import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminMapper {

    //总报名人数
    @Select("SELECT COUNT(DISTINCT userId) FROM direction")
    int countAllUser();

    //具体某个方向人数
    @Select("SELECT COUNT(*) FROM direction WHERE courseId=#{courseId}")
    int countDetailUser(int courseId);

    //更新头像url
    @Update("UPDATE admin SET image=#{image} WHERE adminId=#{adminId}")
    void updateAdminImage(String adminId, String image);
}
