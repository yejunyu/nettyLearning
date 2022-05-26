package webSocket.client;

import io.netty.channel.*;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketHandshakeException;

/**
 * @Author: yejunyu
 * @Date: 2018/11/20
 * @Email: yyyejunyu@gmail.com
 */


@ChannelHandler.Sharable
public class WsClientHandler extends SimpleChannelInboundHandler<Object> {

    private final WebSocketClientHandshaker handshaker;

    private ChannelPromise handshakeFuture;

    public WsClientHandler(WebSocketClientHandshaker handshaker) {
        this.handshaker = handshaker;
    }

    public ChannelFuture getHandshakeFuture() {
        return handshakeFuture;
    }

    private int flag;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelActive");
        handshaker.handshake(ctx.channel());
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelInactive");
        super.channelInactive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        System.out.println("handlerAdded");

        handshakeFuture = ctx.newPromise();

        super.handlerAdded(ctx);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        Channel ch = ctx.channel();
        if (!handshaker.isHandshakeComplete()) {
            try {
                handshaker.finishHandshake(ch, (FullHttpResponse) msg);
                System.out.println("websocket Handshake 完成!");
                handshakeFuture.setSuccess();
            } catch (WebSocketHandshakeException e) {
                System.out.println("websocket连接失败!");
                handshakeFuture.setFailure(e);
            }
        }
        if (flag < 2) {
            new Thread(() -> {
                ctx.channel().writeAndFlush(new TextWebSocketFrame(String.valueOf(flag))).addListener(future -> {
                    flag++;
                    if (future.isDone()) {
                        final Object unused = future.get();
                        System.out.println("xxxx");
                        System.out.println(unused.toString());
                        if (msg instanceof TextWebSocketFrame) {
                            System.out.println("yyyy");
                            final TextWebSocketFrame msg1 = (TextWebSocketFrame) msg;
                            System.out.println(msg1.text());

                        }
                    }
                });
            }).start();


//            ctx.channel().writeAndFlush(new TextWebSocketFrame(String.valueOf(flag))).addListener((ChannelFutureListener) future -> {
//                flag++;
//                if (future.isDone()) {
//                    System.out.println("xxxx");
//                    if (msg instanceof TextWebSocketFrame) {
//                        System.out.println("yyyy");
//                        final TextWebSocketFrame msg1 = (TextWebSocketFrame) msg;
//                        System.out.println(msg1.text());
//
//                    }
//                }
//            });
        }

    }
}
