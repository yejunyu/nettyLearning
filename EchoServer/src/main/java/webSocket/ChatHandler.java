package webSocket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.time.LocalDateTime;

/**
 * @author: YeJunyu
 * @description:
 * @email: yyyejunyu@gmail.com
 * @date: 2020/10/28
 */
public class ChatHandler
        extends SimpleChannelInboundHandler<TextWebSocketFrame/*专门用来在 ws 中处理文本的对象,frame 是消息载体*/> {


    /**
     * 统一管理 所有client的 channel
     */
    private static ChannelGroup clients = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        String content = msg.text();
        System.out.println("接收到的消息: " + content);

//        for (Channel client : clients) {
//            client.writeAndFlush(
//                    new TextWebSocketFrame("[服务器在] " + LocalDateTime.now() + " 接收到消息: " + content)
//            );
//        }
        // 与上面 for 循环效果一致
        clients.writeAndFlush(new TextWebSocketFrame("[服务器在] " + LocalDateTime.now() + " 接收到消息: " + content));
    }

    /**
     * 客户端进来时触发
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        // 都是通过 channel 来收发消息
        // 把 channel 放到 group 里(群接收消息)
        clients.add(ctx.channel());
        System.out.println("客户端连接...");
    }

    /**
     * 客户端离开时触发
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        // 当触发 handlerRemoved,ChannelGroup 会自动移除对应的客户端的 channel
//        clients.remove(ctx.channel());
        System.out.println("客户端断开,channel 对应的长 id 为: " + ctx.channel().id().asLongText());
        System.out.println("客户端断开,channel 对应的 短id 为: " + ctx.channel().id().asShortText());
    }
}
