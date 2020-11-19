package com.yejunyu;

import java.io.IOException;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;

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

        }


        // 循环接收 selector 事件

        // 如果是连接事件

        // 如果是可读事件
    }
}
