package com.geek.geekstudio.websocket;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.geek.geekstudio.util.DateUtil;
import com.geek.geekstudio.websocket.service.UserSessionImpl;
import com.geek.geekstudio.websocket.tool.ToolNettySpringAutowired;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * TextWebSocketHandler--处理文本内容
 * WebSocket处理器，处理websocket连接相关
 */
//多例的
@Slf4j
/*
@Scope("prototype")
@Component
*/
public class TextWebSocketHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    //注入ioc组件方式一
    static UserSessionImpl userSession= ToolNettySpringAutowired.getBean(UserSessionImpl.class);

    /*
    //方式二  要求此类和主类（nettyServer）要放入IOC容器中（@Component）
    //netty线程中拿到的就是就是带有属性的对象
    public static TextWebSocketHandler textWebSocketHandler;
    @Autowired
    UserSessionImpl userSessionImpl;
    //spring IOC初始化单例时在TextWebSocketHandler构造方法后执行的时候进行执行--与bean生命周期相关
    @PostConstruct
    public void init() {
        //获得ioc容器中的TextWebSocketHandler，并用textWebSocketHandler接收
        textWebSocketHandler = this;
        //属性赋值
        textWebSocketHandler.userSessionImpl=this.userSessionImpl;
    }*/

    private boolean readNoReceive =true;
    private String uid;

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("连接建立");
        super.channelRegistered(ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("连接断开");
        super.channelUnregistered(ctx);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelActive....channel为"+ctx.channel());
        super.channelActive(ctx);
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //首次连接是FullHttpRequest，处理uri上的参数
        if (msg instanceof FullHttpRequest) {
            FullHttpRequest request = (FullHttpRequest) msg;
            String uri = request.uri();
            Map<String,String> paramMap=getUrlParams(uri);
            System.out.println("接收到的参数是："+JSON.toJSONString(paramMap));
            uid=paramMap.get("uid");
            userSession.bind(ctx.channel(),uid);
            //如果url包含参数，需要处理
            if(uri.contains("?")){
                String newUri=uri.substring(0,uri.indexOf("?"));
                //修改uri防止升级ws协议失败
                request.setUri(newUri);
            }
        }
        //剩下的释放资源和调用channelRead0方法处理特定方法由父类调用
        super.channelRead(ctx, msg);
    }

    //channel读取完成，且每次读都执行
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        if(readNoReceive){
            LinkedList<String> message = userSession.getMessage(uid);
            System.out.println(message);
            for(String word:message){
                ctx.channel().writeAndFlush(new TextWebSocketFrame(word));
            }
            userSession.clearMessage(uid);
            readNoReceive=false;
        }
        super.channelReadComplete(ctx);
    }


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        String word=msg.text();
        System.out.println(word);
        //前端发送的是心跳包
        if("heart".equals(word)){
            TextWebSocketFrame back=new TextWebSocketFrame("heart");
            ctx.writeAndFlush(back);
            return;
        }
        //{"toId":"20","word":"xmas"}
        //解析内容
        JSONObject jsonObject = JSON.parseObject(word);
        //追加发送人(防止串改)
        jsonObject.put("fromId", this.uid);
        jsonObject.put("time", DateUtil.creatDate());
        String toId = (String) jsonObject.remove("toId");
        if("all".equals(toId)){
            for(String id:userSession.allUser){
                sendMessage(id,jsonObject);
            }
        }else if(userSession.courseUser.containsKey(toId)){
            ArrayList<String> ids = userSession.courseUser.get(toId);
            for(String id:ids){
                sendMessage(id,jsonObject);
            }
        }else {
            sendMessage(toId,jsonObject);
        }
    }

    //向一个user 的channel推送消息
    private void sendMessage(String toId,JSONObject jsonObject){
        //将消息传输到对应toId用户--用户在线
        if (userSession.containChannel(toId)) {
            System.out.println(jsonObject.toJSONString());
            System.out.println("channel"+userSession.getChannel(toId));
            userSession.getChannel(toId).writeAndFlush(new TextWebSocketFrame(jsonObject.toJSONString()));
        } else {
            //用户在线，将消息存直接存储在应用中/或存入redis中
            userSession.putMessage(toId,jsonObject.toJSONString());
        }
    }

    //客户端连接断开
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("与客户端断开连接，通道关闭！");
        if(userSession.containChannel(uid)) {
            userSession.unbind(uid);
        }
    }

    //客户端因为异常连接断开
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("出现异常，channel断开--异常原因："+cause.getMessage());
        cause.printStackTrace();
        if(userSession.containChannel(uid)) {
            userSession.unbind(uid);
        }
    }


    //找到url中的参数
    private static Map<String,String> getUrlParams(String url){
        Map<String,String> map = new HashMap<>();
        url = url.replace("?",";");
        if (!url.contains(";")){
            return map;
        }
        if (url.split(";").length > 0){
            String[] arr = url.split(";")[1].split("&");
            for (String s : arr){
                String key = s.split("=")[0];
                String value = s.split("=")[1];
                map.put(key,value);
            }
        }
        return  map;
    }
}