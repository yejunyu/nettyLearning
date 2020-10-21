package httpServer;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpObject;
import io.netty.util.CharsetUtil;

/**
 * @author: YeJunyu
 * @description: 自定义的助手类
 * @email: yyyejunyu@gmail.com
 * @date: 2020/10/21
 */
public class CustomHandler extends SimpleChannelInboundHandler<HttpObject> {

    // read0 代表从缓冲区读取数据
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
        Channel channel = ctx.channel();
        System.out.println(channel.remoteAddress());
        //
        ByteBuf buffer = Unpooled.copiedBuffer("hello netty", CharsetUtil.UTF_8);
        // 构建 http 响应
        FullHttpResponse response = new DefaultFullHttpResponse();
        // 把数据刷到 client
        channel.writeAndFlush(response);
    }
}
