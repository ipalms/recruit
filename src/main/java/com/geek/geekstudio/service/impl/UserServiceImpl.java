package com.geek.geekstudio.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.geek.geekstudio.exception.*;
import com.geek.geekstudio.mapper.AdminMapper;
import com.geek.geekstudio.mapper.UserMapper;
import com.geek.geekstudio.model.po.AdminPO;
import com.geek.geekstudio.model.po.UserPO;
import com.geek.geekstudio.model.vo.AdminVO;
import com.geek.geekstudio.model.vo.RestInfo;
import com.geek.geekstudio.model.vo.UserVO;
import com.geek.geekstudio.service.JavaMailService;
import com.geek.geekstudio.service.UserService;
import com.geek.geekstudio.util.DateUtil;
import com.geek.geekstudio.util.DozerUtil;
import com.geek.geekstudio.util.UuidUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 *用户服务具体实现类
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    //token默认失效时间为4小时
    private long expiresAtTime = 4*60*60*1000;

    @Autowired
    UserMapper userMapper;
    @Autowired
    AdminMapper adminMapper;
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
    public RestInfo register(UserPO userPO,String activeCode) throws UserRegisteredException, EmailCodeWrongException {
        if(userMapper.queryUserByUserId(userPO.getUserId())!=null){
            //再检验一遍学号是否被注册了
            throw new UserRegisteredException();
        }
        String active =(String) redisTemplate.opsForValue().get(userPO.getUserId());
        if(active==null||active.length()==0){
            throw new EmailCodeWrongException("请点击再次发送邮件");
        }
        if(!activeCode.equals(active)){
            throw new EmailCodeWrongException("验证码输入错误，请重新输入");
        }
        userPO.setRegisterTime(DateUtil.creatDate());
        userPO.setIntroduce("亲还没有个人介绍呢~~");
        userPO.setGrade("2021");
        userMapper.addUser(userPO);
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
        long time=System.currentTimeMillis();
        String token;
        Object user=userMapper.queryUserByUserIdAndPassword(userId,password);
        if(user==null){//该用户不存在于新生表
            user=adminMapper.queryAdminByUserIdAndPassword(userId,password);
        }if(user==null){
            //用户或密码错误
            throw new UsernameOrPasswordIncorrectException();
        }
        token= JWT.create()
                .withIssuedAt(new Date(time)) //生成签名的时间
                .withExpiresAt(new Date(time + expiresAtTime))//签名过期的时间
                // 签名的观众 也可以理解谁接受签名的
                .withAudience(userId)
                // 这里是以用户密码作为密钥
                .sign(Algorithm.HMAC256(password));
        redisTemplate.opsForValue().set(token,user);
        return RestInfo.success(user+"token:"+token);
    }

    /**
     *注销登录用户
     */
    @Override
    public RestInfo logout(String token) throws NoTokenException {
        if(redisTemplate.opsForValue().get(token)==null){
            throw new NoTokenException("用户未登录！");
        }
        redisTemplate.delete(token);
        return RestInfo.success("用户已注销");
    }

    /**
     * 用户操作时token过期，为其再生成一个token
     */
    @Override
    public RestInfo resetToken(String token) {
        long time=System.currentTimeMillis();
        String userId,password;
        Object user=redisTemplate.opsForValue().get(token);
        if(user instanceof UserPO){
            userId=((UserPO) user).getUserId();
            password=((UserPO) user).getPassword();
        }else {
            userId=((AdminPO) user).getAdminId();
            password=((AdminPO) user).getPassword();
        }
        redisTemplate.delete(token);
        //重新生成的Token
        token= JWT.create()
                .withIssuedAt(new Date(time)) //生成签名的时间
                .withExpiresAt(new Date(time + expiresAtTime))//签名过期的时间
                // 签名的观众 也可以理解谁接受签名的
                .withAudience(userId)
                // 这里是以用户密码作为密钥
                .sign(Algorithm.HMAC256(password));
        redisTemplate.opsForValue().set(token,user);
        return RestInfo.success(token);
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
