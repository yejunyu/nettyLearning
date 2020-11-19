package com.yejunyu;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @author: YeJunyu
 * @description:
 * @email: yyyejunyu@gmail.com
 * @date: 2020/11/18
 */
public class NioTest {

    public static void main(String[] args) throws IOException {
        // 多个 port
        int[] ports = new int[]{
                5000, 5001, 5002, 5003, 5004
        };
        // selector
        Selector selector = Selector.open();
        // channel 注册到 selector 上,selector 关注 accept 事件
        for (int i = 0; i < ports.length; i++) {
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            // 设为非阻塞
            serverSocketChannel.configureBlocking(false);
            // socket 标准流程
            ServerSocket socket = serverSocketChannel.socket();
            socket.bind(new InetSocketAddress(ports[i]));
            // socket 绑定好之后需要把 channel注册到 selector 上
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        }
        // 循环接收 selector 事件
        while (true) {
            int selectNums = selector.select();
            System.out.println("select numbers: " + selectNums);
            // key set
            Set<SelectionKey> selectionKeySet = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeySet.iterator();
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                // 如果是连接事件
                if (selectionKey.isAcceptable()) {
                    // 拿到 channel, channel 注册读事件
                    ServerSocketChannel channel = (ServerSocketChannel) selectionKey.channel();
                    // accept 请求,和 bio 一样的流程
                    SocketChannel socketChannel = channel.accept();
                    // 依旧要设置非阻塞
                    socketChannel.configureBlocking(false);
                    // socketChannel 注册读事件
                    socketChannel.register(selector, SelectionKey.OP_READ);
                    // 连接事件处理完了一定要销毁
                    iterator.remove();
                } else if (selectionKey.isReadable()) {
                    // 如果是可读事件
                    // 拿到 channel
                    SocketChannel channel = (SocketChannel) selectionKey.channel();
                    int byteRead = 0;
                    while (true) {
                        // 读写靠 buffer
                        ByteBuffer buffer = ByteBuffer.allocate(512);
                        // 循环读一定要 clear
                        buffer.clear();
                        int read = channel.read(buffer);
                        if (read <= 0) {
                            break;
                        }
                        // 翻转成写模式
                        buffer.flip();
                        channel.write(buffer);
                        byteRead += read;
                        System.out.println(" 读取: " + byteRead + ", 来自: " + channel);
                        // 用完销毁
                        iterator.remove();
                    }
                }
            }

        }
    }
}
