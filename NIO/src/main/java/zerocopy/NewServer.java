package zerocopy;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * @author yejunyu
 * @date 2020/12/12
 * @desc
 */
public class NewServer {

    public static void main(String[] args) throws IOException {
        InetSocketAddress address = new InetSocketAddress(9999);

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        ServerSocket serverSocket = serverSocketChannel.socket();
        serverSocket.setReuseAddress(true);
        serverSocket.bind(address);

        ByteBuffer byteBuffer = ByteBuffer.allocate(4096);

        while (true) {
            SocketChannel socketChannel = serverSocketChannel.accept();
            socketChannel.configureBlocking(true);

            int readCount = 0;
            while (-1 != readCount) {
                try {
                    readCount = socketChannel.read(byteBuffer);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                byteBuffer.rewind();
            }
        }
    }
}
