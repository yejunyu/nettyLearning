package httpServer;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * @author : YeJunyu
 * @description :
 * @email : yyyejunyu@gmail.com
 * @date : 2020/10/19
 */
public class HelloChannelInitializer extends ChannelInitializer<SocketChannel> {


    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        // 通过 socketChannel 获取对应的管道
        ChannelPipeline pipeline = ch.pipeline();
        // netty 自己提供的助手类
        // http 的编解码器
        pipeline.addLast("HttpServerCodec", new HttpServerCodec());
        // 添加自己的助手类
        pipeline.addLast("customHandler", new CustomHandler());
    }
}
