package com.geek.geekstudio.mapper;

import com.geek.geekstudio.model.dto.UserDTO;
import com.geek.geekstudio.model.po.UserPO;
import com.geek.geekstudio.model.vo.UserInfo;
import com.geek.geekstudio.model.vo.UserVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

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
    @Insert("INSERT INTO user (userId,userName,mail,password,major,sex,image,introduce,registerTime,grade,receiveMail,firstLogin)" +
            " VALUES (#{userId},#{userName},#{mail},#{password},#{major},#{sex},#{image},#{introduce},#{registerTime},#{grade},#{receiveMail},#{firstLogin})")
    void addUser(UserDTO userDTO);

    /**
     *通过id 密码查新生
     */
    @Select("SELECT id,userId,userName,mail,major,sex,image,introduce,grade,registerTime,receiveMail,firstLogin" +
            "  FROM user WHERE userId=#{userId} AND password=#{password}")
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

    /**
     *更新用户的头像地址
     */
    @Update("UPDATE user SET image=#{image} WHERE userId=#{userId}")
    void updateUserImage(String userId, String image);

    /**
     *查询个人是否接收日常邮件
     */
    @Select("SELECT receiveMail FROM user WHERE userId=#{userId}")
    int queryReceiveMailStatus(String userId);

    /**
     *改变接收日常邮件的状态
     */
    @Update("UPDATE user SET receiveMail=#{receiveMail} WHERE userId=#{userId}")
    void changeReceiveMailStatus(@Param("userId") String userId,@Param("receiveMail") int receiveMail);

    /**
     *获取接收邮件大一同学的邮箱   如果courseId为null的话，邮箱需要去重
     */
    List<String> queryMails(@Param("courseId") Integer courseId);

    /**
     *通过id返回邮箱
     */
    @Select("SELECT mail FROM user WHERE userId=#{userId} AND receiveMail=1 ")
    String queryMailByUserId(String userId);

    /**
     *具体某个方向人数
     */
    @Select("SELECT COUNT(DISTINCT userId) total FROM direction WHERE (courseId = #{courseId} or #{courseId}=0)")
    int countDetailUser(int courseId);

    /**
     *查询学生信息详情
     */
    @Select("SELECT DISTINCT u.userId,u.userName,u.mail,u.major,u.image,u.grade FROM user u, direction d WHERE (d.courseId = #{courseId} or #{courseId}=0) AND u.userId=d.userId LIMIT #{start},#{rows}")
    List<UserVO> queryUsersInfo(@Param("courseId")int courseId,@Param("start") int start,@Param("rows") int rows);

    /**
     *查询某一方向所有学生信息详情
     */
    @Select("SELECT DISTINCT u.userId,u.userName,u.mail,u.major,u.image,u.grade FROM user u, direction d WHERE d.courseId = #{courseId}  AND u.userId=d.userId")
    List<UserInfo> queryAllUsersInfo(int courseId);

    /**
     * 查询注册的所有用户的学号
     */
    @Select("SELECT userId FROM user")
    List<String> queryAllUserId();

    /**
     * 查询某一方向所有学生的学号
     */
    @Select("SELECT DISTINCT u.userId FROM user u, direction d WHERE d.courseId = #{courseId}  AND u.userId=d.userId")
    ArrayList<String> queryCourseUserId(int courseId);

    /**
     * 更新登录状态
     */
    @Update("UPDATE user SET firstLogin=0 WHERE userId=#{userId}")
    void updateLoginRecord(String userId);

    /**
     * 更改用户id
     */
    @Update("UPDATE user SET userName=#{userName} WHERE userId=#{userId}")
    void updateUserName(String userId, String userName);
}
