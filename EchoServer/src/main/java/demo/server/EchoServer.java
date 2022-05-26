package demo.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

/**
 * @Author: yejunyu
 * @Date: 2018/11/20
 * @Email: yyyejunyu@gmail.com
 */
public class EchoServer {

    private final int port;

    public EchoServer(int port) {
        this.port = port;
    }

    public void start() throws InterruptedException {
        // 创建EventGroup,epoll的基于事件驱动的响应
        NioEventLoopGroup boss = new NioEventLoopGroup(1);
        // 默认是 2 倍核数线程
        NioEventLoopGroup worker = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            // 创建ServerBootstrap
            b.group(boss, worker)
                    // 指定NIO使用的Channel
                    .channel(NioServerSocketChannel.class)
                    // 设置socket地址使用的端口
                    .localAddress(new InetSocketAddress(port))
                    // 添加Handler到Channel的ChannelPipeline
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) {
                            socketChannel.pipeline()
                                    // http 编解码
//                                    .addLast("codec", new HttpServerCodec())
                                    // httpContent 压缩
//                                    .addLast("compressor", new HttpContentCompressor())
                                    // http 消息聚合 (request,response)
//                                    .addLast("aggregator", new HttpObjectAggregator(65535))
                                    // 自定义handler
                                    .addLast(new EchoServerHandler());
                        }
                    })
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            // 绑定服务,sync等待服务器关闭
            ChannelFuture f = b.bind().sync();
            System.out.println(EchoServer.class.getName() + " started and listen on " + f.channel().localAddress());
            // 关闭channel
            f.channel().closeFuture().sync();
        } finally {
            // 关闭EventLoopGroup,释放所有资源
            boss.shutdownGracefully().sync();
            worker.shutdownGracefully().sync();
        }

    }

    public static void main(String[] args) throws InterruptedException {
        // 与bio Server一样,设置一个端口
        int port = 8080;
        // server启动
        new EchoServer(port).start();

    }
}
