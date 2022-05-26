package com.yejunyu.bilibili.server;

import com.yejunyu.bilibili.LifeCycle;
import com.yejunyu.bilibili.models.Proto;
import com.yejunyu.bilibili.processors.Processor;
import com.yejunyu.bilibili.server.handlers.ProtoHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshakerFactory;
import io.netty.handler.codec.http.websocketx.WebSocketVersion;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketClientCompressionHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;

import java.net.InetSocketAddress;
import java.net.URI;

/**
 * @author : YeJunyu
 * @description :
 * @email : yyyejunyu@gmail.com
 * @date : 2022/5/18
 */
public class BClient implements LifeCycle {

    private final URI uri;
    private final String ip;
    private final int port;
    private EventLoopGroup eventLoopGroup;
    private ChannelFuture connect;
    private final String authBody;

    private Bootstrap bootstrap;
    private Processor processor;

    public BClient(AuthClient authClient, Processor processor) {
        this.uri = URI.create(authClient.getWsUrl());
        this.ip = uri.getHost();
        this.port = uri.getPort();
        this.authBody = authClient.getAuthBody();
        this.processor = processor;
        init();
    }


    @Override
    public void init() {
        bootstrap = new Bootstrap();
        eventLoopGroup = new NioEventLoopGroup();
        // ws handshaker
        final WebSocketClientHandshaker handshaker = WebSocketClientHandshakerFactory.newHandshaker(uri, WebSocketVersion.V13, null, true, new DefaultHttpHeaders());
        final ProtoHandler protoHandler = new ProtoHandler(handshaker, authBody, processor);
        bootstrap.group(eventLoopGroup)
                // 指定 channel 为 niochannel
                .channel(NioSocketChannel.class)
                .remoteAddress(new InetSocketAddress(ip, port))
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(
                                new HttpClientCodec(),
                                new HttpObjectAggregator(1024 * 8),
                                WebSocketClientCompressionHandler.INSTANCE,
                                new IdleStateHandler(0, 29, 0),
                                new HeartBeatHandler(),
                                protoHandler);
                    }
                });

    }

    @Override
    public void start() {
        try {
            this.connect = bootstrap.connect().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("blibili client start ....");
    }

    @Override
    public void shutdown() {
        eventLoopGroup.shutdownGracefully();
        connect.channel().closeFuture();
    }

    static class HeartBeatHandler extends ChannelDuplexHandler {
        @Override
        public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
            if (evt instanceof IdleStateEvent) {
                System.out.println("心跳~~~");
                IdleStateEvent event = (IdleStateEvent) evt;
                if (event.state().equals(IdleState.WRITER_IDLE)) {
                    Proto proto = new Proto();
                    proto.setOp(Proto.OP_HEARTBEAT);
                    // 发送心跳包
                    ctx.writeAndFlush(new BinaryWebSocketFrame(Unpooled.copiedBuffer(proto.pack())));
                }
            }
            super.userEventTriggered(ctx, evt);
        }
    }
}
