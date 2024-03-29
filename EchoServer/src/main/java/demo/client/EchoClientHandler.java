package demo.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

/**
 * @Author: yejunyu
 * @Date: 2018/11/20
 * @Email: yyyejunyu@gmail.com
 */
@ChannelHandler.Sharable
public class EchoClientHandler extends SimpleChannelInboundHandler<ByteBuf> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {
        System.out.println("Client received: " + byteBuf.toString(CharsetUtil.UTF_8));
        channelHandlerContext.writeAndFlush(byteBuf.toString()+"x");
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 检测到活动的时候发送消息
        // Unpooled 是用来 Creating a copied buffer(深拷贝)
        ctx.writeAndFlush(Unpooled.copiedBuffer("Netty rocksx", CharsetUtil.UTF_8));
        super.channelActive(ctx);
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Client received: channelRegistered" );
        ctx.writeAndFlush(Unpooled.copiedBuffer("Netty rocksxxxx", CharsetUtil.UTF_8));
        super.channelRegistered(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx,
                                Throwable cause) {
        System.out.println("1111111");
        cause.printStackTrace();
        ctx.close();
    }
}
