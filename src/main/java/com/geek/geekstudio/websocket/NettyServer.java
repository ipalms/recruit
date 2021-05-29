package com.geek.geekstudio.websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketServerCompressionHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * NettyServer Netty服务器配置
 */
@Slf4j
public class NettyServer {

    public void start() throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap sb = new ServerBootstrap();
            //全连接队列长度--当接收请求量过大时连接放入全连接队列
            sb.option(ChannelOption.SO_BACKLOG, 1024);
            sb.group(bossGroup, workerGroup) // 绑定线程池
                    .channel(NioServerSocketChannel.class) // 指定使用的channel
                    .childHandler(new ChannelInitializer<SocketChannel>() { // 绑定客户端连接时候触发操作
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline()
                                    //websocket协议本身是基于http协议的，所以这边也要使用http解编码器 转成httpRequest消息
                                    .addLast(new HttpServerCodec())
                                    //以块的方式来写的处理器  -- 分块向客户端写数据，防止发送大文件时导致内存溢出
                                    .addLast(new ChunkedWriteHandler())
                                    //将HttpMessage和HttpContents聚合到一个完整的FullHttpRequest或FullHttpResponse中
                                    //具体是FullHttpRequest对象还是FullHttpResponse对象取决于是请求还是响应
                                    //需要放到HttpServerCodec这个处理器后面
                                    .addLast(new HttpObjectAggregator(10240))
                                    .addLast(new ReadTimeoutHandler(1000, TimeUnit.SECONDS))
                                    // webSocket 数据压缩扩展，当添加这个的时候WebSocketServerProtocolHandler的第三个参数需要设置成true
                                    .addLast(new WebSocketServerCompressionHandler())
                                    //1小时内没有读取到消息关闭连接--close通道
                                    //超时产生ReadTimeoutException异常
                                    //.addLast(new ReadTimeoutHandler(1,TimeUnit.HOURS))
                                    //自定义处理TextWebSocketFrame帧的handler--放在WebSocketServerProtocolHandler之前是为了提取出url的参数（同时做参数校验）
                                    //防止升级到websocket协议失败（也可以在这里放入处理FullHttpRequest对象的处理器，将文本处理器放止最后）
                                    .addLast(new TextWebSocketHandler())
                                    // 自定义处理器 - 处理 web socket 二进制消息
                                    /*.addLast(new BinaryWebSocketFrameHandler())*/
                                    //服务器端向外暴露的websocket端点，当客户端传递比较大的对象时，maxFrameSize参数的值需要调大
                                    //该handler可以协助做handler的自动的动态热插拔，在升级ws协议成功后会去除掉处理http请求的handler
                                    .addLast(new WebSocketServerProtocolHandler("/websocket", null, true, 65536 * 10));
                        }
                    });
            ChannelFuture cf = sb.bind(8549).sync(); // 服务器异步创建绑定
            cf.channel().closeFuture().sync(); // 关闭服务器通道
        } finally {
            bossGroup.shutdownGracefully().sync(); // 释放线程池资源
            workerGroup.shutdownGracefully().sync();
        }
    }
}
