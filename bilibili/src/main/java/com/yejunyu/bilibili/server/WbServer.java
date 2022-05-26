package com.yejunyu.bilibili.server;

import com.yejunyu.bilibili.BConfig;
import com.yejunyu.bilibili.LifeCycle;
import com.yejunyu.bilibili.server.handlers.WSServerInitialzer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.Data;

import java.net.InetSocketAddress;

/**
 * @author : YeJunyu
 * @description :
 * @email : yyyejunyu@gmail.com
 * @date : 2022/5/20
 */
@Data
public class WbServer implements LifeCycle {

    private NioEventLoopGroup bossGroup;
    private NioEventLoopGroup workGroup;
    private ServerBootstrap bootstrap;
    private ChannelFuture channelFuture;

    public WbServer() {
        init();
    }

    @Override
    public void init() {
        bossGroup = new NioEventLoopGroup(1);
        workGroup = new NioEventLoopGroup();
        bootstrap = new ServerBootstrap();
        // 两个线程组
        bootstrap.group(bossGroup, workGroup)
                // 指定 channel 为 niochannel
                .channel(NioServerSocketChannel.class)
                // worker 线程组要做的处理
                // 所有 handler 聚集的初始化器
                .childHandler(new WSServerInitialzer())
                // 就是keepalive, 对应用层没什么用
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                // 让关闭连接释放的端口今早可使用
                .childOption(ChannelOption.SO_REUSEADDR, true)
                // TCP接收缓冲区的容量上限
                .childOption(ChannelOption.SO_RCVBUF, 65535)
                // TCP发送缓冲区的容量上限
                .childOption(ChannelOption.SO_SNDBUF, 65535)
                // 握手等待队列和 accept 队列之和
                .childOption(ChannelOption.SO_BACKLOG, 1024)
                // 开启 netty 的池化内存
                .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);

    }

    @Override
    public void start() {
        // 用同步方法来启动和关闭服务
        try {
            channelFuture = bootstrap.bind(new InetSocketAddress(BConfig.port)).sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void shutdown() {
        bossGroup.shutdownGracefully();
        workGroup.shutdownGracefully();
        channelFuture.channel().closeFuture();
    }
}
