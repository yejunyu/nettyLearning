package com.yejunyu.bilibili.processors;

import com.alibaba.fastjson.JSON;
import com.yejunyu.bilibili.context.WbRequestWrapper;
import com.yejunyu.bilibili.models.LiveResponse;
import com.yejunyu.bilibili.models.Proto;
import com.yejunyu.bilibili.server.ChatHandler;
import com.yejunyu.bilibili.server.WbServer;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

/**
 * @author : YeJunyu
 * @description : 默认执行器
 * @email : yyyejunyu@gmail.com
 * @date : 2022/5/19
 */
public class CoreProcessor implements Processor {

    private WbServer wbServer;

    public CoreProcessor(WbServer wbServer) {
        this.wbServer = wbServer;
    }

    @Override
    public void process(WbRequestWrapper event) {
        final ChannelHandlerContext ctx = event.getCtx();
        final Proto proto = event.getProto();
        try {
            // 1. 解析 proto 转成内部对象
            final String body = proto.getBody();
            final LiveResponse liveResponse = JSON.parseObject(body, LiveResponse.class);
            // 2. 执行链式逻辑
            ChatHandler.users.writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(liveResponse)));

        } catch (Exception e) {

        } finally {

        }
    }

}
