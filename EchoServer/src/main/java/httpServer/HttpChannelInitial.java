package httpServer;

import io.netty.channel.ChannelInitializer;
import io.netty.handler.codec.http.HttpMessage;
import io.netty.handler.codec.http.HttpObject;

/**
 * @author : YeJunyu
 * @description :
 * @email : yyyejunyu@gmail.com
 * @date : 2020/10/19
 */
public class HttpChannelInitial extends ChannelInitializer<HttpMessage> {


    @Override
    protected void initChannel(HttpObject ch) throws Exception {

    }
}
