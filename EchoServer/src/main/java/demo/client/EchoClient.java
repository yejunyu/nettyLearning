package demo.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

/**
 * @Author: yejunyu
 * @Date: 2018/11/20
 * @Email: yyyejunyu@gmail.com
 */
public class EchoClient {

    private final String host;
    private final int port;

    public EchoClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void start() throws InterruptedException {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            // 创建BootStrap
            Bootstrap b = new Bootstrap();
            // 指定EventLoopGroup来处理客户端事件,由于我们使用NIO传输,所以用的是NioEventLoopGroup
            b.group(group)
                    // 用于nio传输的channel
                    .channel(NioSocketChannel.class)
                    // 设置服务器的host和port
                    .remoteAddress(new InetSocketAddress(host, port))
                    // 当建立一个连接和一个新通道时,创建添加到EchoClientHandler实例到channel pipeline
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new EchoClientHandler());
                        }
                    });
            // 连接远程
            ChannelFuture f = b.connect().sync();
            // 阻塞直到Channel关闭
            f.channel().closeFuture().sync();
        } finally {
            // 关闭线程池,释放所有资源
            group.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        final String host = "127.0.0.1";
        final int port = 8080;
        new EchoClient(host, port).start();
    }
}
