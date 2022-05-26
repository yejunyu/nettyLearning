package webSocket.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshakerFactory;
import io.netty.handler.codec.http.websocketx.WebSocketVersion;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketClientCompressionHandler;

import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * @Author: yejunyu
 * @Date: 2018/11/20
 * @Email: yyyejunyu@gmail.com
 */
public class WsClient {

    private final String host;
    private final int port;

    public WsClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void start() throws InterruptedException, URISyntaxException {
        final URI uri = new URI("ws://127.0.0.1:8080/ws");
        final WebSocketClientHandshaker handshaker = WebSocketClientHandshakerFactory.newHandshaker(uri, WebSocketVersion.V13, null, true, new DefaultHttpHeaders());
        WsClientHandler wsClientHandler = new WsClientHandler(handshaker);
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            // 创建BootStrap
            Bootstrap b = new Bootstrap();
            // 指定EventLoopGroup来处理客户端事件,由于我们使用NIO传输,所以用的是NioEventLoopGroup
            b.group(group)
                    // 用于nio传输的channel
                    .channel(NioSocketChannel.class)
                    // 设置服务器的host和port
                    .remoteAddress(new InetSocketAddress(host, port))
                    // 当建立一个连接和一个新通道时,创建添加到EchoClientHandler实例到channel pipeline
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(
                                    new HttpClientCodec(),
                                    new HttpObjectAggregator(1024 * 8),
                                    WebSocketClientCompressionHandler.INSTANCE,
                                    wsClientHandler);
                        }
                    });
            // 连接远程
            ChannelFuture f = b.connect().sync();
            // 阻塞直到Channel关闭
            f.channel().closeFuture().sync();
        } finally {
            // 关闭线程池,释放所有资源
            group.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws InterruptedException, URISyntaxException {
        final String host = "127.0.0.1";
        final int port = 8080;
        new WsClient(host, port).start();
    }
}
