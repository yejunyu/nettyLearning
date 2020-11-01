package demo.server;

import constant.Constants;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import zookeeper.ZookeeperFactory;

import java.net.InetAddress;

/**
 * @author: yejunyu
 * date: 2018/11/19
 */
public class NettyServer {

    public static void main(String[] args) throws Exception {
        // 监听线程组,默认为1
        EventLoopGroup parentGroup = new NioEventLoopGroup();
        // 处理线程组,默认为cpu * 2
        EventLoopGroup childGroup = new NioEventLoopGroup();
        try {
            // server需要一个ServerBootstrap
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(parentGroup, childGroup);
            // 并发量非常高的时候,允许128个channel排队
            bootstrap.option(ChannelOption.SO_BACKLOG, 128)
                    // 心跳机制,默认就是false(需要自己实现心跳包的逻辑)
                    .childOption(ChannelOption.SO_KEEPALIVE, false)
                    // 绑定通道
                    .channel(NioServerSocketChannel.class)
                    // 初始化通道
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            // 解码器,长度65535,设置分隔符
                            socketChannel.pipeline()
                                    .addLast(new DelimiterBasedFrameDecoder(65535, Delimiters.lineDelimiter()))
                                    // rpc二进制流转字符串
                                    .addLast(new StringDecoder())
                                    .addLast(new SimpleServerHandler())
                                    .addLast(new StringDecoder());
                        }
                    });
            ChannelFuture f = bootstrap.bind(8080).sync();
            // 本机地址注册到 zk
            CuratorFramework client = ZookeeperFactory.create();
            InetAddress netAddress = InetAddress.getLocalHost();
            client.create().withMode(CreateMode.EPHEMERAL).forPath(Constants.SERVER_PATH + netAddress.getHostAddress());
            f.channel().closeFuture().sync();
        } finally {
            childGroup.shutdownGracefully();
            parentGroup.shutdownGracefully();
        }

    }
}
