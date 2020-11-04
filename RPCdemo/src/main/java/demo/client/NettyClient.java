package demo.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.net.InetSocketAddress;
import java.util.Arrays;

/**
 * @Author: yejunyu
 * @Date: 2018/11/20
 * @Email: yyyejunyu@gmail.com
 */
public class NettyClient {

    private final String host;
    private final int port;

    public NettyClient(String host, int port) {
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
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    // 设置服务器的host和port
                    .remoteAddress(new InetSocketAddress(host, port))
                    // 当建立一个连接和一个新通道时,创建添加到EchoClientHandler实例到channel pipeline
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            pipeline.addLast(new DelimiterBasedFrameDecoder(Integer.MAX_VALUE, Delimiters.lineDelimiter()[0]))
                                    .addLast(new StringDecoder())
                                    .addLast(new SimpleClientHandler())
                                    .addLast(new StringEncoder());
                        }

                    });
            // 连接远程
            ChannelFuture f = b.connect().sync();
            f.channel().writeAndFlush("hello server\r\n");
            // 阻塞直到Channel关闭
            f.channel().closeFuture().sync();
        } finally {
            // 关闭线程池,释放所有资源
            group.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println(Arrays.toString(Delimiters.lineDelimiter()));
        final String host = "127.0.0.1";
        final int port = 8080;
        new NettyClient(host, port).start();
    }
}
