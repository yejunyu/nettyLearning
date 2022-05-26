package com.yejunyu.bilibili.context;

import com.yejunyu.bilibili.models.Proto;
import io.netty.channel.ChannelHandlerContext;
import lombok.Data;

/**
 * @author : YeJunyu
 * @description : 转发消息的请求包装类
 * @email : yyyejunyu@gmail.com
 * @date : 2022/5/20
 */
@Data
public class WbRequestWrapper {

    private Proto proto;

    private ChannelHandlerContext ctx;

}
