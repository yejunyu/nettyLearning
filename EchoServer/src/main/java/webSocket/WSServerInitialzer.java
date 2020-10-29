package webSocket;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

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
        pipeline.addLast(new HttpServerCodec());
        // pippeline 大数据流传输支持 chunk
        pipeline.addLast(new ChunkedWriteHandler());
        // httpObject 聚合器,聚合成 fullhttpReqest或 fullhttpResponse
        pipeline.addLast(new HttpObjectAggregator(1024 * 64));
        // =============以上是用于支持 http 协议(因为 ws是基于 http),下面是 ws 的支持===============

        // ws 协议支持用于指定客户端访问的路由
        pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));
        // 自定义 handler
        pipeline.addLast(new ChatHandler());
    }
}
