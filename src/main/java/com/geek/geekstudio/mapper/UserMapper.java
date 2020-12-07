package com.geek.geekstudio.mapper;

import com.geek.geekstudio.model.dto.UserDTO;
import com.geek.geekstudio.model.po.UserPO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

/**
 * 定义对User表的数据库操作
 */
@Repository
public interface UserMapper {
    /**
     *通过学号查新生信息
     */
    @Select("SELECT * FROM user WHERE userId=#{userId}")
    UserPO queryUserByUserId(String userId);

    /**
     *通过邮箱查新生信息
     */
    @Select("SELECT * FROM user WHERE mail=#{mail}")
    UserPO queryUserByMail(String mail);

    /**
     *注册一个新生用户
     */
    @Insert("INSERT INTO user (userId,userName,mail,password,major,sex,image,introduce,registerTime,grade)" +
            " VALUES (#{userId},#{userName},#{mail},#{password},#{major},#{sex},#{image},#{introduce},#{registerTime},#{grade})")
    void addUser(UserDTO userDTO);

    /**
     *通过id 密码查新生
     */
    @Select("SELECT * FROM user WHERE userId=#{userId} AND password=#{password}")
    UserPO queryUserByUserIdAndPassword(@Param("userId") String userId,@Param("password") String password);

    /**
     *修改密码
     */
    @Update("UPDATE user SET password=#{newPassword} WHERE userId=#{userId}")
    void updatePassword(@Param("userId")String userId,@Param("newPassword") String newPassword);

    /**
     *设置介绍
     */
    @Update("UPDATE user SET introduce=#{introduce} WHERE userId=#{userId}")
    void updateIntroduce(String userId, String introduce);
}
