package com.yejunyu.bilibili.server.handlers;

import com.yejunyu.bilibili.context.WbRequestWrapper;
import com.yejunyu.bilibili.models.Proto;
import com.yejunyu.bilibili.processors.Processor;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketHandshakeException;

import java.util.Arrays;

/**
 * @author: YeJunyu
 * @description: <p>https://blog.csdn.net/canxiangzhang/article/details/99743553</p>
 * @email: yyyejunyu@gmail.com
 * @date: 2020/10/28
 */
public class ProtoHandler
        extends SimpleChannelInboundHandler<Object/*专门用来在 ws 中处理文本的对象,frame 是消息载体*/> {

    private final WebSocketClientHandshaker handshaker;

    private ChannelPromise handshakeFuture;

    private String authBody;

    private Processor processor;

    public ProtoHandler(WebSocketClientHandshaker handshaker, String authBody, Processor processor) {
        this.handshaker = handshaker;
        this.authBody = authBody;
        this.processor = processor;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        Channel ch = ctx.channel();
        System.out.println("=========");
        System.out.println(msg);
        System.out.println(msg.getClass());
        System.out.println("=========");
        if (!handshaker.isHandshakeComplete()) {
            try {
                handshaker.finishHandshake(ch, (FullHttpResponse) msg);
                System.out.println("websocket Handshake 完成!");
                handshakeFuture.setSuccess();
                Proto proto = new Proto();
                proto.setOp(Proto.OP_AUTH);
                proto.setBody(authBody);
                ctx.writeAndFlush(new BinaryWebSocketFrame(Unpooled.copiedBuffer(proto.pack()))).addListener( future -> {
                    // fixme future 取不到数据
                    if (future.isSuccess()) {
                        System.out.println("auth 发送成功");
                    }
                });
            } catch (WebSocketHandshakeException e) {
                System.out.println("websocket连接失败!");
                handshakeFuture.setFailure(e);
            }
        }
        if (msg instanceof BinaryWebSocketFrame) {
            final BinaryWebSocketFrame frame = (BinaryWebSocketFrame) msg;
            Proto proto = new Proto();
            // fixme 坑点, 不能传原始数据要复制一份
            final Proto unpack = proto.unpack(Unpooled.copiedBuffer(frame.content()));
            System.out.println(unpack);
            if (unpack.getOp() == Proto.OP_SEND_SMS_REPLY) {
                // 直播间的反馈消息进行处理
                final WbRequestWrapper wbRequestWrapper = new WbRequestWrapper();
                wbRequestWrapper.setProto(proto);
                wbRequestWrapper.setCtx(ctx);
                processor.process(wbRequestWrapper);
            }
        }
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("ProtoHandler#channelRegistered");
        super.channelRegistered(ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("ProtoHandler#channelUnregistered");
        super.channelUnregistered(ctx);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("ProtoHandler#channelActive");
        handshaker.handshake(ctx.channel());
        super.channelActive(ctx);
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        System.out.println("ProtoHandler#handlerAdded");
        handshakeFuture = ctx.newPromise();
        super.handlerAdded(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("ProtoHandler#channelInactive");
        super.channelInactive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("ProtoHandler#exceptionCaught: " + Arrays.toString(cause.getStackTrace()));
        super.exceptionCaught(ctx, cause);
    }
}
