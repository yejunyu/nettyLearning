package webSocket;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

/**
 * @author: YeJunyu
 * @description:
 * @email: yyyejunyu@gmail.com
 * @date: 2020/10/28
 */
public class WSServerInitialzer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        // webSocket 基于 http 协议,首先需要 http 编解码

        // pippeline 大数据流传输支持 chunk

        // httpObject 聚合器,聚合成 fullhttpReqest或 fullhttpResponse

        // =============以上是用于支持 http 协议(因为 ws是基于 http),下面是 ws 的支持===============

        // ws 协议支持用于指定客户端访问的路由

        // 自定义 handler
    }
}
