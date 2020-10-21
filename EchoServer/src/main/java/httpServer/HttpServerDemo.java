package httpServer;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

/**
 * @author : YeJunyu
 * @description :
 * @email : yyyejunyu@gmail.com
 * @date : 2020/10/19
 */
public class HttpServerDemo {

    private final static int PORT = 8080;

    public HttpServerDemo() {
    }


    public void start(){
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            // 两个线程组
            bootstrap.group(bossGroup,workGroup)
                    // 指定 channel 为 niochannel
                    .channel(NioServerSocketChannel.class)
                    // worker 线程组要做的处理
                    .childHandler(new ChannelInitializer<>() {
                    });
            // 用同步方法来启动和关闭服务
            ChannelFuture channelFuture = bootstrap.bind(new InetSocketAddress(PORT)).sync();
            channelFuture.channel().closeFuture().sync();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }

    }

    public static void main(String[] args) {

    }
}
