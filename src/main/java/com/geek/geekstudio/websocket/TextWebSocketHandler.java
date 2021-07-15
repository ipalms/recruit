package com.geek.geekstudio.websocket;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.geek.geekstudio.util.DateUtil;
import com.geek.geekstudio.util.TokenUtil;
import com.geek.geekstudio.websocket.service.UserSessionImpl;
import com.geek.geekstudio.websocket.tool.ToolNettySpringAutowired;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_LENGTH;

/**
 * TextWebSocketHandler--处理文本内容
 * WebSocket处理器，处理websocket连接相关
 */
//多例的
@Slf4j
public class TextWebSocketHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    //注入ioc组件方式一
    static UserSessionImpl userSession = ToolNettySpringAutowired.getBean(UserSessionImpl.class);

    static RedisTemplate redisTemplate = ToolNettySpringAutowired.getBean("redisTemplate", RedisTemplate.class);

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

    private volatile boolean readNoReceive = true;
    private String uid;


    /*    //后于channelRegistered（）方法的调用，此时channel已经注册了读事件
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelActive....channel为" + ctx.channel());
        super.channelActive(ctx);
    }*/


    @Override
    //@SuppressWarnings("all")
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //首次连接是FullHttpRequest，处理uri上的参数
        if (msg instanceof FullHttpRequest) {
            FullHttpRequest request = (FullHttpRequest) msg;
            String uri = request.uri();
            Map<String, String> paramMap = getUrlParams(uri);
            //System.out.println("接收到的参数是：" + JSON.toJSONString(paramMap));
            //进行身份验证
            String token = paramMap.get("token");
            boolean result = checkUserLegality(token);
            //验证失败
            if(!result){
                //关闭通道（channel）
                ctx.channel().close();
                //return防止方法继续执行到下面的其他逻辑--因为关闭channel这个方法是异步的
                return;
            }
            uid = paramMap.get("uid");
            userSession.bind(ctx.channel(), uid);
            //如果url包含参数，需要处理
            if (uri.contains("?")) {
                String newUri = uri.substring(0, uri.indexOf("?"));
                //修改uri防止升级ws协议失败
                request.setUri(newUri);
            }
        }
        //剩下的释放资源和调用channelRead0方法处理特定方法由父类调用
        super.channelRead(ctx, msg);
    }

    //检验用户身份的合法性
    private boolean checkUserLegality(String token) {
        if (token == null) {
            return false;
        }
        String userId = (String) redisTemplate.opsForValue().get(token);
        //userId为空说明token过期 或 token是伪造的
        return userId != null;
    }

    //channel读取完成【传播链都调用了channelRead()以后】，且每次读都执行
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        if (readNoReceive&&uid!=null) {
            ArrayList<String> message = userSession.getMessage(uid);
            //System.out.println("未读消息："+message);
            for (String word : message) {
                ctx.channel().writeAndFlush(new TextWebSocketFrame(word));
            }
            //userSession.clearMessage(uid);
            readNoReceive = false;
        }
        super.channelReadComplete(ctx);
    }


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        String word = msg.text();
        System.out.println(word);
        //前端发送的是心跳包
        if ("ping".equals(word)) {
            TextWebSocketFrame back = new TextWebSocketFrame("pong");
            ctx.writeAndFlush(back);
            //结束方法--防止线程继续执行下去
            return;
        }
        //{"toId":"20","word":"xmas"}
        //解析内容
        JSONObject jsonObject = JSON.parseObject(word);
        //追加发送人(防止串改)
        jsonObject.put("fromId", this.uid);
        jsonObject.put("time", DateUtil.creatDate());
        String toId = (String) jsonObject.remove("toId");
        if ("all".equals(toId)) {
            for (String id : userSession.allUser) {
                userSession.sendOneMessage(id, jsonObject);
            }
        } else if (userSession.courseUser.containsKey(toId)) {
            ArrayList<String> ids = userSession.courseUser.get(toId);
            for (String id : ids) {
                userSession.sendOneMessage(id, jsonObject);
            }
        } else {
            userSession.sendOneMessage(toId, jsonObject);
        }
    }

    //客户端连接断开
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("与客户端断开连接，通道关闭！");
        if (uid!=null&&userSession.containChannel(uid)) {
            userSession.unbind(uid);
        }
    }

    //客户端因为异常连接断开
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.info("出现异常，channel断开--异常原因:{}",cause.getMessage());
        cause.printStackTrace();
        if (uid!=null&&userSession.containChannel(uid)) {
            userSession.unbind(uid);
        }
    }


    //找到url中的参数
    private static Map<String, String> getUrlParams(String url) {
        Map<String, String> map = new HashMap<>();
        url = url.replace("?", ";");
        if (!url.contains(";")) {
            return map;
        }
        if (url.split(";").length > 0) {
            String[] arr = url.split(";")[1].split("&");
            for (String s : arr) {
                String key = s.split("=")[0];
                String value = s.split("=")[1];
                map.put(key, value);
            }
        }
        return map;
    }
}