package com.geek.geekstudio.service.impl;

import com.geek.geekstudio.exception.*;
import com.geek.geekstudio.mapper.CourseMapper;
import com.geek.geekstudio.mapper.SuperAdminMapper;
import com.geek.geekstudio.mapper.DirectionMapper;
import com.geek.geekstudio.mapper.UserMapper;
import com.geek.geekstudio.model.dto.DirectionDTO;
import com.geek.geekstudio.model.dto.UserDTO;
import com.geek.geekstudio.model.po.AdminPO;
import com.geek.geekstudio.model.po.UserPO;
import com.geek.geekstudio.model.vo.DirectionVO;
import com.geek.geekstudio.model.vo.RestInfo;
import com.geek.geekstudio.service.UserService;
import com.geek.geekstudio.util.DateUtil;
import com.geek.geekstudio.util.FileUtil;
import com.geek.geekstudio.util.TokenUtil;
import com.geek.geekstudio.websocket.service.UserSessionImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 *用户服务具体实现类
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    //token默认失效时间为4小时
    private static long expiresAtTime = 4*60*60*1000;
    //refreshToken默认失效时间为8天
    private static long longExpiresAtTime = 8*24*60*60*1000;

    @Autowired
    UserMapper userMapper;
    @Autowired
    SuperAdminMapper adminMapper;
    @Autowired
    DirectionMapper directionMapper;
    @Autowired
    CourseMapper courseMapper;
    @Autowired
    FileUtil fileUtil;
    @Autowired
    JavaMailServiceImpl javaMailServiceImpl;
    @Autowired
    CourseServiceImpl courseService;
    @Autowired
    UserSessionImpl userSession;
    @Autowired
    RedisTemplate<Object,Object> redisTemplate;  //k-v都是对象的

    private static final String defaultPicture="/image/default.jpg";

    /**
     * 新生注册
     */
    @Override
    //可以让事件在遇到非运行时异常时也回滚 回到没注册状态
    @Transactional(rollbackFor = Exception.class)
    public RestInfo register(UserDTO userDTO) throws UserRegisteredException, EmailCodeWrongException {
        String active;
        String grade;
        String image;
        if(userMapper.queryUserByUserId(userDTO.getUserId())!=null){
            //再检验一遍学号是否被注册了
            throw new UserRegisteredException();
        }
        active=(String) redisTemplate.opsForValue().get("code-"+userDTO.getUserId());
        if(active==null||active.length()==0){
            throw new EmailCodeWrongException("请点击再次发送邮件");
        }
        if(!userDTO.getActiveCode().equals(active)){
            throw new EmailCodeWrongException("验证码输入错误，请重新输入");
        }
        userDTO.setRegisterTime(DateUtil.creatDate());
        userDTO.setIntroduce("亲还没有个人介绍呢~~");
        grade = userDTO.getUserId().substring(0, 4);
        try {
            Integer.valueOf(grade);
            image="/image/picture"+(Integer.parseInt(userDTO.getUserId())%12)+".jpg";
        } catch (Exception e) {
            grade=null;
            image=defaultPicture;
        }
        userDTO.setGrade(grade);
        userDTO.setImage(image);
        //默认接收日常邮件
        userDTO.setReceiveMail("yes");
        userMapper.addUser(userDTO);
        //维护allUser变量
        userSession.allUser.add(userDTO.getUserId());
        return RestInfo.success("注册成功，亲可以去登录了！",null);
    }

    @Override
    public RestInfo sendActiveMail(String userId,String mail,Integer codeType) throws  MessagingException {
        javaMailServiceImpl.sendActiveMail(userId,mail,codeType);
        return RestInfo.success("邮件发送成功",null);
    }

    /**
     * 统一登录（管理员和新生）
     */
    @Override
    public RestInfo login(String userId, String password) throws UsernameOrPasswordIncorrectException {
        //生成token 和 refreshToken
        String token,refreshToken;
        //返回前端数据
        Map<String,Object> data=new HashMap<>();
        String type ;
        Object user=userMapper.queryUserByUserIdAndPassword(userId,password);
        if(user!=null){
            type="student";
            UserPO userPO=(UserPO)user;
            //加入新生的专业选择
            RestInfo restInfo=courseService.queryMyCourse(userPO.getUserId());
            userPO.setDirectionVOList((List<DirectionVO>)restInfo.getData());
            userPO.setImage(fileUtil.getFileUrl(userPO.getImage()));
        }else{//该用户不存在于新生表
            user=adminMapper.queryAdminByUserIdAndPassword(userId,password);
            if(user!=null){
                AdminPO adminPO=(AdminPO)user;
                type=adminPO.getType();
                adminPO.setImage(fileUtil.getFileUrl(adminPO.getImage()));
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
     *更新用户信息
     */
    @Override
    public RestInfo updateInfo(String token) {
        String userId = (String)redisTemplate.opsForValue().get(token);
        if(userId==null) {
            throw new ExpiredJwtException(null,null,"token过期了");
        }
        Object user=userMapper.queryUserByUserId(userId);
        if(user!=null){
            UserPO userPO=(UserPO)user;
            //加入新生的专业选择
            RestInfo restInfo=courseService.queryMyCourse(userPO.getUserId());
            userPO.setDirectionVOList((List<DirectionVO>)restInfo.getData());
            userPO.setImage(fileUtil.getFileUrl(userPO.getImage()));
        }else{
            user=adminMapper.queryAdminByAdminId(userId);
            AdminPO adminPO=(AdminPO)user;
            adminPO.setImage(fileUtil.getFileUrl(adminPO.getImage()));
        }
        return RestInfo.success(user);
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
    public RestInfo resetToken(String refreshToken) throws PermissionDeniedException {
        String token;
        Object user;
        //返回前端数据
        Map<Object,Object> data=new HashMap<>();
        //检验refreshToken是否过期
        Claims message;
        try {
            message= TokenUtil.parseJWT(refreshToken);
        } catch (ExpiredJwtException e) {
            throw new ExpiredJwtException(e.getHeader(),e.getClaims(),"refreshToken过期了");
        }catch (Exception k){
            throw new PermissionDeniedException("请登录");
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
     *用户修改密码
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public RestInfo resetPassword(UserDTO userDTO, String token) {
        userMapper.updatePassword(userDTO.getUserId(),userDTO.getNewPassword());
        //删除原先的token-refreshToken
        redisTemplate.delete(token);
        redisTemplate.delete(userDTO.getRefreshToken());
        return RestInfo.success("修改密码成功！",null);
    }

    /**
     * 用户忘记密码校验操作
     * 根据userId查询用户，判断其mail邮箱与所给的是相同
     */
    @Override
    public RestInfo checkUserLegality(UserDTO userDTO) throws ParameterError {
        UserPO userPO=userMapper.queryUserByUserId(userDTO.getUserId());
        if(userPO==null){
            throw new ParameterError("学号不存在！");
        }
        if(!userDTO.getMail().equals(userPO.getMail())){
            throw new ParameterError("请检查学号或邮箱正确性！");
        }
        return RestInfo.success("可发送找回密码邮件",null);
    }

    /**
     * 用户忘记密码-设置新密码
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestInfo findBackPassword(UserDTO userDTO) throws EmailCodeWrongException {
        String active=(String) redisTemplate.opsForValue().get("code-"+userDTO.getUserId());
        if(active==null||active.length()==0){
            throw new EmailCodeWrongException("请点击再次发送邮件");
        }
        if(!userDTO.getActiveCode().equals(active)){
            throw new EmailCodeWrongException("验证码输入错误，请重新输入");
        }
        userMapper.updatePassword(userDTO.getUserId(),userDTO.getNewPassword());
        return RestInfo.success("更改密码成功，请您用新密码登录！",null);
    }

    /**
     * 用户设置简介
     */
    @Override
    public RestInfo setIntroduce(UserDTO userDTO) {
        userMapper.updateIntroduce(userDTO.getUserId(),userDTO.getIntroduce());
        return RestInfo.success("设置签名成功！",null);
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
        //维护courseUser变量
        userSession.courseUser.get(userSession.courseRelation.get(directionDTO.getCourseId())).add(directionDTO.getUserId());
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

    /**
     * 查询当前登录用户是否接收日常邮件
     */
    @Override
    public RestInfo queryReceiveMailStatus(String userId) {
        String status=userMapper.queryReceiveMailStatus(userId);
        return RestInfo.success("用户是否接收日常邮件",status);
    }

    /**
     * 改变接收日常邮件的状态
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestInfo changeReceiveMailStatus(String userId) {
        String status=userMapper.queryReceiveMailStatus(userId);
        String newStatus;
        if(status.equals("yes")){
            newStatus="no";
        }else {
            newStatus="yes";
        }
        userMapper.changeReceiveMailStatus(userId,newStatus);
        return RestInfo.success("改变接收邮件状态成功！");
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
