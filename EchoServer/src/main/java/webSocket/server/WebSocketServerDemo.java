package webSocket.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.net.InetSocketAddress;

/**
 * @author : YeJunyu
 * @description :
 * @email : yyyejunyu@gmail.com
 * @date : 2020/10/19
 */
public class WebSocketServerDemo {

    private final static int PORT = 8888;

    public WebSocketServerDemo() {
    }


    public void start() {


        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            // 两个线程组
            bootstrap.group(bossGroup, workGroup)
                    // 指定 channel 为 niochannel
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new LoggingHandler(LogLevel.INFO))
                    // worker 线程组要做的处理
                    // 所有 handler 聚集的初始化器
                    .childHandler(new WSServerInitialzer())
                    // 就是keepalive, 对应用层没什么用
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    // 让关闭连接释放的端口今早可使用
                    .childOption(ChannelOption.SO_REUSEADDR, true)
                    // TCP接收缓冲区的容量上限
                    .childOption(ChannelOption.SO_RCVBUF, 1048576)
                    // TCP发送缓冲区的容量上限
                    .childOption(ChannelOption.SO_SNDBUF, 1048576)
                    // 握手等待队列和 accept 队列之和
                    .childOption(ChannelOption.SO_BACKLOG, 1024)
                    // 开启 netty 的池化内存
                    .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
            // 用同步方法来启动和关闭服务
            ChannelFuture channelFuture = bootstrap.bind(new InetSocketAddress(PORT)).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }

    }

    public static void main(String[] args) {
        WebSocketServerDemo httpServerDemo = new WebSocketServerDemo();
        httpServerDemo.start();
    }
}
