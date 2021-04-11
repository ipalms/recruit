package com.geek.geekstudio.websocket.service;

import com.geek.geekstudio.mapper.CourseMapper;
import com.geek.geekstudio.mapper.UserMapper;
import com.geek.geekstudio.model.po.CoursePO;
import com.geek.geekstudio.service.CourseService;
import io.netty.channel.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * channel通道对象不能通过序列化保存到redis之间实现netty集群间的通信--因为channel是保存与线程（用户通道联系的）
 */
@Component
public class UserSessionImpl implements UserSession {

    @Autowired
    UserMapper userMapper;

    @Autowired
    CourseMapper courseMapper;

    //存储用户--channel关系映射
    private final Map<String, Channel> usernameChannelMap = new ConcurrentHashMap<>();

    //存储未接收到的信息
    private final Map<String, LinkedList<String>> userMessageMap =new ConcurrentHashMap<>();

    //存储所有用户的id（user表的）
    public List<String> allUser=new CopyOnWriteArrayList<>();
    //存储每门课程和对应的用户id
    public Map<String, ArrayList<String>> courseUser=new ConcurrentHashMap<>();

    //初始化id信息
    @PostConstruct
    public void init(){
        this.allUser.addAll(userMapper.queryAllUserId());
        List<CoursePO> coursePOList = courseMapper.queryCourse();
        for(CoursePO coursePO:coursePOList){
             this.courseUser.put(coursePO.getCourseName(),userMapper.queryCourseUserId(coursePO.getId()));
        }
    }



    //给channel通道绑定一些属性，如果需要
//   private final Map<Channel,Map<String,Object>> channelAttributesMap = new ConcurrentHashMap<>();

    @Override
    public void bind(Channel channel, String uid) {
        usernameChannelMap.put(uid, channel);
        if(!userMessageMap.containsKey(uid)){
            userMessageMap.put(uid,new LinkedList<String>());
        }
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
    }

    //@Override
    public void clearMessage(String uid){
        userMessageMap.get(uid).clear();
    }

   /* @Override
    public String toString() {
        return usernameChannelMap.toString();
    }*/
}
