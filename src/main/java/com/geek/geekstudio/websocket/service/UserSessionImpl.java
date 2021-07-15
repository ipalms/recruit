package com.geek.geekstudio.websocket.service;

import com.alibaba.fastjson.JSONObject;
import com.geek.geekstudio.mapper.CourseMapper;
import com.geek.geekstudio.mapper.UserMapper;
import com.geek.geekstudio.model.po.CoursePO;
import com.geek.geekstudio.service.CourseService;
import com.geek.geekstudio.util.DateUtil;
import com.sun.xml.bind.v2.runtime.output.SAXOutput;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * channel通道对象不能通过序列化保存到redis之间实现netty集群间的通信--因为channel是保存与线程（用户通道联系的）
 */
@Component
@Slf4j
public class UserSessionImpl implements UserSession {

    @Autowired
    UserMapper userMapper;

    @Autowired
    CourseMapper courseMapper;

    @Autowired
    RedisTemplate<Object,Object> redisTemplate;

    //存储用户--channel关系映射
    private final Map<String, Channel> usernameChannelMap = new ConcurrentHashMap<>();

    //存储未接收到的信息 java堆内存版
    //private final Map<String, LinkedList<String>> userMessageMap =new ConcurrentHashMap<>();

    //存储所有用户的id（user表的）
    public List<String> allUser=new CopyOnWriteArrayList<>();
    //存储每门课程和对应的用户id
    public Map<String, ArrayList<String>> courseUser=new ConcurrentHashMap<>();
    //存储课程id到课程名的映射
    public Map<Integer,String> courseRelation=new HashMap<>();

    //初始化id信息
    @PostConstruct
    public void init(){
        this.allUser.addAll(userMapper.queryAllUserId());
        List<CoursePO> coursePOList = courseMapper.queryCourse();
        for(CoursePO coursePO:coursePOList){
            courseRelation.put(coursePO.getCourseId(),coursePO.getCourseName());
            this.courseUser.put(coursePO.getCourseName(),userMapper.queryCourseUserId(coursePO.getId()));
        }
    }

    public boolean isOnline(String userId){
        return usernameChannelMap.containsKey(userId);
    }

    @Override
    public void bind(Channel channel, String uid) {
        usernameChannelMap.put(uid, channel);
/*        if(!userMessageMap.containsKey(uid)){
            userMessageMap.put(uid,new LinkedList<String>());
        }*/
    }

    @Override
    public void unbind(String uid) {
        usernameChannelMap.remove(uid);
    }

    @Override
    public Channel getChannel(String uid) {
        return usernameChannelMap.get(uid);
    }

    @Override
    public boolean containChannel(String uid){
        return usernameChannelMap.containsKey(uid);
    }

/*  java堆内存版
    @Override
    public void putMessage(String toId, String msg) {
        if(!userMessageMap.containsKey(toId)){
            userMessageMap.put(toId,new LinkedList<String>());
        }
        userMessageMap.get(toId).add(msg);
    }

    @Override
    public LinkedList<String> getMessage(String uid) {
        return userMessageMap.get(uid);

    //@Override
    public void clearMessage(String uid){
        userMessageMap.get(uid).clear();
    }
    }*/

    //测试 序列化JSONObject对象和仅仅只序列化字符串相比，序列化对象平均每条记录大22字节（存储一些对象全类名信息）
    @Override
    public void putMessage(String toId,String msg) {
        redisTemplate.opsForList().rightPush(toId,msg);
    }

    @Override
    public ArrayList<String> getMessage(String uid) {
        Object p= null;
        try {
            p = redisTemplate.opsForList().range(uid,0,-1);
        } catch (Exception e) {
            log.info("消息反序列化失败");
        }
        redisTemplate.delete(uid);
        return new ArrayList<>((List<String>)p);
    }


    //向springboot应用开放一个通知用户消息的方法（websocket）
    public void sendMessage(String toId,String fromId,String word){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("fromId", fromId);
        jsonObject.put("time", DateUtil.creatDate());
        jsonObject.put("word", word);
        if("all".equals(toId)){
            for(String id:allUser){
                sendOneMessage(id,jsonObject);
            }
        }else if(courseUser.containsKey(toId)){
            ArrayList<String> ids = courseUser.get(toId);
            for(String id:ids){
                sendOneMessage(id,jsonObject);
            }
        }else {
            sendOneMessage(toId,jsonObject);
        }
    }

    //向一个user 的channel推送消息
    public void sendOneMessage(String toId, JSONObject jsonObject){
        //将消息传输到对应toId用户--用户在线
        if (usernameChannelMap.containsKey(toId)) {
            System.out.println("消息："+jsonObject.toJSONString());
            System.out.println("channel"+usernameChannelMap.get(toId));
            usernameChannelMap.get(toId).writeAndFlush(new TextWebSocketFrame(jsonObject.toJSONString()));
        } else {
            //用户在线，将消息存直接存储在应用中
            //（缺点应用关闭的时候消息就未读丢失了/可以应用关闭的时候调用接口将消息存入redis,启动的时候消息读出）
            //存入redis中（操作麻烦，比直接操作存入java堆内存慢一点）
            this.putMessage(toId,jsonObject.toJSONString());
        }
    }
}
