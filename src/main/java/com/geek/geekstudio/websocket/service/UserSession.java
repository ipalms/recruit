package com.geek.geekstudio.websocket.service;


import com.alibaba.fastjson.JSONObject;
import io.netty.channel.Channel;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * 用户会话管理接口
 */
public interface UserSession {

    /**
     * 绑定会话
     * @param channel 哪个 channel 要绑定会话
     * @param uid 会话绑定用户
     */
    void bind(Channel channel, String uid);

    /**
     * 解绑会话
     * @param uid 会话解绑用户id
     */
    void unbind(String uid);

    /**
     * 判断用户是否在线
     * @param uid 用户的id
     * @return
     */
    boolean containChannel(String uid);

    /**
     * 根据用户名获取 channel
     * @param uid 用户名
     * @return channel
     */
    Channel getChannel(String uid);

    /**
     * 存储未读消息
     * @param toId
     * @param msg
     */
    void putMessage(String toId,String msg);

    /**
     * 读取历史消息，并清空已读
     * @param uid
     * @return
     */
    ArrayList<String> getMessage(String uid);

    /**
     * 发送消息
     * @param toId
     * @param fromId
     * @param word
     */
    void sendMessage(String toId,String fromId,String word);

    /**
     * 对某个对象发送消息
     * @param toId
     * @param jsonObject
     */
    void sendOneMessage(String toId, JSONObject jsonObject);
}
