package zerocopy;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

/**
 * @author yejunyu
 * @date 2020/12/12
 * @desc
 */
public class NewClient {

    public static void main(String[] args) throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress("localhost", 9999));

        String fileName = "/Users/yejunyu/Desktop/面向对象葵花宝典++思想、技巧与实践.pdf";
        FileChannel fileChannel = new FileInputStream(fileName).getChannel();
        long startTime = System.currentTimeMillis();
        long total = fileChannel.transferTo(0, fileChannel.size(), socketChannel);

        System.out.println("发送字节数: " + total + " , 耗时 :" + (System.currentTimeMillis() - startTime));
        fileChannel.close();
    }
}
