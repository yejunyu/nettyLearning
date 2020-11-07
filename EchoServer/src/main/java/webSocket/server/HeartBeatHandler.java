package webSocket.server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * @author: YeJunyu
 * @description: 继承ChannelInboundHandlerAdapter从而不需要实现 channelRead0 方法
 * @email: yyyejunyu@gmail.com
 * @date: 2020/10/28
 */
public class HeartBeatHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        // 用于检测用户(读/写 空闲)
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;

            switch (event.state()) {
                case READER_IDLE:
                    System.out.println("进入读空闲...");
                    break;
                case WRITER_IDLE:
                    System.out.println("进入写空闲...");
                    break;
                case ALL_IDLE:
                    Channel channel = ctx.channel();
                    System.out.println("关闭前 user 数为: " + ChatHandler.users.size());
                    // 关闭无用 channel 以防资源浪费
                    channel.close();
                    System.out.println("channel 关闭后 user 数为: " + ChatHandler.users.size());
                    break;
                default:
                    System.out.println("error");
            }
        }
    }
}
