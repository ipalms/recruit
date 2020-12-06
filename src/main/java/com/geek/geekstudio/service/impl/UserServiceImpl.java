package com.geek.geekstudio.service.impl;

import com.geek.geekstudio.exception.*;
import com.geek.geekstudio.mapper.AdminMapper;
import com.geek.geekstudio.mapper.DirectionMapper;
import com.geek.geekstudio.mapper.UserMapper;
import com.geek.geekstudio.model.dto.DirectionDTO;
import com.geek.geekstudio.model.dto.UserDTO;
import com.geek.geekstudio.model.po.AdminPO;
import com.geek.geekstudio.model.po.UserPO;
import com.geek.geekstudio.model.vo.AdminVO;
import com.geek.geekstudio.model.vo.RestInfo;
import com.geek.geekstudio.model.vo.UserVO;
import com.geek.geekstudio.service.JavaMailService;
import com.geek.geekstudio.service.UserService;
import com.geek.geekstudio.util.DateUtil;
import com.geek.geekstudio.util.DozerUtil;
import com.geek.geekstudio.util.TokenUtil;
import com.geek.geekstudio.util.UuidUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 *用户服务具体实现类
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    //token默认失效时间为4小时
    private long expiresAtTime = 4*60*60*1000;
    //refreshToken默认失效时间为8天
    private long longExpiresAtTime = 8*24*60*60*1000;

    @Autowired
    UserMapper userMapper;
    @Autowired
    AdminMapper adminMapper;
    @Autowired
    DirectionMapper directionMapper;
    @Autowired
    JavaMailServiceImpl javaMailServiceImpl;
    @Autowired
    RedisTemplate<Object,Object> redisTemplate;  //k-v都是对象的

    /**
     * 新生注册
     */
    @Override
    //可以让事件在遇到非运行时异常时也回滚 回到没注册状态
    @Transactional(rollbackFor = Exception.class)
    public RestInfo register(UserDTO userDTO) throws UserRegisteredException, EmailCodeWrongException {
        if(userMapper.queryUserByUserId(userDTO.getUserId())!=null){
            //再检验一遍学号是否被注册了
            throw new UserRegisteredException();
        }
        String active =(String) redisTemplate.opsForValue().get(userDTO.getUserId());
        if(active==null||active.length()==0){
            throw new EmailCodeWrongException("请点击再次发送邮件");
        }
        if(!userDTO.getActiveCode().equals(active)){
            throw new EmailCodeWrongException("验证码输入错误，请重新输入");
        }
        userDTO.setRegisterTime(DateUtil.creatDate());
        userDTO.setIntroduce("亲还没有个人介绍呢~~");
        userDTO.setGrade("2021");
        userMapper.addUser(userDTO);
        return RestInfo.success("注册成功，亲可以去登录了！",null);
    }

    @Override
    public RestInfo sendActiveMail(String userId,String mail) throws  MessagingException {
        javaMailServiceImpl.sendActiveMail(userId,mail);
        return RestInfo.success("激活邮件发送成功",null);
    }

    /**
     * 统一登录（管理员和新生）
     */
    @Override
    public RestInfo login(String userId, String password) throws UsernameOrPasswordIncorrectException {
        //生成token 和 refreshToken
        String token,refreshToken;
        //返回前端数据
        Map<Object,Object> data=new HashMap<Object,Object>();
        String type = null;
        Object user=userMapper.queryUserByUserIdAndPassword(userId,password);
        if(user!=null){
            type="student";
        }else{//该用户不存在于新生表
            user=adminMapper.queryAdminByUserIdAndPassword(userId,password);
            if(user!=null){
                type=((AdminPO)user).getType();
            }else {
                //用户或密码错误
                throw new UsernameOrPasswordIncorrectException();
            }
        }
        token= TokenUtil.createJWT(userId,type,expiresAtTime);
        refreshToken= TokenUtil.createJWT(userId,type,longExpiresAtTime);
        //4小时后删除
        redisTemplate.opsForValue().set(token,userId,4, TimeUnit.HOURS);
        //8天后删除
        redisTemplate.opsForValue().set(refreshToken,userId,8, TimeUnit.DAYS);
        data.put("user",user);
        data.put("token",token);
        data.put("refreshToken",refreshToken);
        return RestInfo.success(data);
    }

    /**
     *注销登录用户
     */
    @Override
    public RestInfo logout(String token, String refreshToken) throws NoTokenException {
        if(redisTemplate.opsForValue().get(token)==null){
            //似乎没必要
            throw new NoTokenException("用户未登录！");
        }
        redisTemplate.delete(token);
        redisTemplate.delete(refreshToken);
        return RestInfo.success("用户已注销");
    }

    /**
     * 用户操作时token过期，为其再生成一个token
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public RestInfo resetToken(String refreshToken) {
        String token;
        Object user;
        //返回前端数据
        Map<Object,Object> data=new HashMap<Object,Object>();
        //检验refreshToken是否过期
        Claims message=null;
        try {
            message= TokenUtil.parseJWT(refreshToken);
        } catch (ExpiredJwtException e) {
            throw new ExpiredJwtException(e.getHeader(),e.getClaims(),"refreshToken过期了");
        }
        String userId= message.getSubject();
        String type=message.get("type", String.class);
        //删除旧的refreshToken
        redisTemplate.delete(refreshToken);
        //生成新的token 和 refreshToken
        token= TokenUtil.createJWT(userId,type,expiresAtTime);
        refreshToken= TokenUtil.createJWT(userId,type,longExpiresAtTime);
        //4小时后删除
        redisTemplate.opsForValue().set(token,userId,4, TimeUnit.HOURS);
        //8天后删除
        redisTemplate.opsForValue().set(refreshToken,userId,8, TimeUnit.DAYS);
        if("student".equals(type)){
            user=userMapper.queryUserByUserId(userId);
        }else {
            user=adminMapper.queryAdminByAdminId(userId);
        }
        data.put("user",user);
        data.put("token",token);
        data.put("refreshToken",refreshToken);
        return RestInfo.success("重新生成token成功！",data);
    }

    /**
     *大一同学选择方向
     */
    @Override
    public RestInfo chooseCourse(DirectionDTO directionDTO) throws ParameterError {
        if(directionMapper.queryByUserIdAndCourseId(directionDTO.getUserId(),directionDTO.getCourseId())!=null){
            throw new ParameterError("该方向已选择，不可重复添加！");
        }
        directionDTO.setAddTime(DateUtil.creatDate());
        directionMapper.addDirection(directionDTO);
        return RestInfo.success("选择方向成功",null);
    }

    /**
     *大一同学撤销选择方向
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public RestInfo delCourse(DirectionDTO directionDTO) throws ParameterError {
        if(directionMapper.queryByUserIdAndCourseId(directionDTO.getUserId(),directionDTO.getCourseId())==null){
            throw new ParameterError("该方向还未选择");
        }
        directionMapper.delByUserIdAndCourseId(directionDTO.getUserId(),directionDTO.getCourseId());
        return RestInfo.success("撤销已方向成功",null);
    }

    /*
    （使用链接的形式激活）
    @Override
    public RestInfo active(String activeCode) throws RecruitException {
        //根据激活码查询用户对象
        UserPO user = userMapper.findByCode(activeCode);
        if(user == null){
            throw new NoUserException();
        }
        //更新激活状态
        userMapper.updateStatus(user);
        return RestInfo.success(user);
    }*/

    /*
    （使用链接的形式激活）
    public RestInfo register(UserPO userPO) throws UserRegisteredException, MessagingException {
        long time=System.currentTimeMillis();
        if(userMapper.queryUserByUserId(userPO.getUserId())!=null){
            //该学号已被注册
            throw new UserRegisteredException();
        }
        userPO.setState("no");  //用户未激活
        userPO.setActiveCode(UuidUtil.getUuid());
        userPO.setRegisterTime(DateUtil.creatDate());
        userPO.setIntroduce("亲还没有个人介绍呢~~");
        userMapper.addUser(userPO);
        javaMailService.sendActiveMail(userPO.getMail(),userPO.getActiveCode());
        return RestInfo.success("注册成功，亲可以去登录了！",null);
    }*/


    /**
     *给用户设置token属性
     *//*
    @Override
    public RestInfo login(String userId, String password) throws UsernameOrPasswordIncorrectException {
        UserPO userPO=userMapper.queryAdminByUserIdAndPassword(userId,password);
        if(userPO!=null){
            restoreUserInRedis(userPO);
        }else{
            //该用户不存在于新生表
            AdminPO adminPO=adminMapper.queryAdminByUserIdAndPassword(userId,password);
           if(adminPO!=null){
               restoreUserInRedis(adminPO);
           }else {
               //用户或密码错误
               throw new UsernameOrPasswordIncorrectException();
           }
        }
        return null;
    }

    private void restoreUserInRedis(UserPO userPO) {
        long time=System.currentTimeMillis();
        String token= JWT.create()
                .withIssuedAt(new Date(time)) //生成签名的时间
                .withExpiresAt(new Date(time + expiresAtTime))//签名过期的时间
                // 签名的观众 也可以理解谁接受签名的
                .withAudience(userPO.getUserId())
                // 这里是以用户密码作为密钥
                .sign(Algorithm.HMAC256(userPO.getPassword()));
        UserVO userVO = DozerUtil.getDozerBeanMapper().map(userPO,UserVO.class);
        userVO.setToken(token);
        redisTemplate.opsForValue().set(token,userVO);
        log.info("新生登录：userId:"+userPO.getUserId()+", password:"+userPO.getPassword());
    }

    public void restoreUserInRedis(AdminPO adminPO){
        long time=System.currentTimeMillis();
        String token= JWT.create()
                .withIssuedAt(new Date(time)) //生成签名的时间
                .withExpiresAt(new Date(time + expiresAtTime))//签名过期的时间
                // 签名的观众 也可以理解谁接受签名的
                .withAudience(adminPO.getAdminId())
                // 这里是以用户密码作为密钥
                .sign(Algorithm.HMAC256(adminPO.getPassword()));
        AdminVO adminVO = DozerUtil.getDozerBeanMapper().map(adminPO,AdminVO.class);
        adminVO.setToken(token);
        redisTemplate.opsForValue().set(token,adminVO);
        log.info("管理员登录：userId:"+adminVO.getAdminId()+", password:"+adminVO.getPassword());
    }*/
}
