package demo.server;

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

/**
 * 标识这类的实例之间可以在channel里共享(保证线程安全)
 */
@ChannelHandler.Sharable
public class EchoServerHandler extends
        SimpleChannelInboundHandler<ByteBuf> {


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        // 日志消息输出到控制台
        System.out.println("Server received: " + msg.toString(CharsetUtil.UTF_8));
        // 将所接手的消息返回给发送者,注意这里还没有冲刷数据
        ctx.writeAndFlush(msg.toString()+"x");
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        // 冲刷所有待审消息到远程节点.关闭通道后,操作完成
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello",CharsetUtil.UTF_8));
//        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER)
//                .addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx,
                                Throwable cause) {
        // 打印异常堆栈跟踪
        cause.printStackTrace();
        // 关闭通道
        ctx.close();
    }
}
