package nioTest;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.channels.FileChannel;
import java.security.SecureRandom;

/**
 * @author: YeJunyu
 * @description:
 * @email: yyyejunyu@gmail.com
 * @date: 2020/11/16
 */
public class NioTest {

    /**
     * buffer 的读写
     * 8 种基本类型(除了 bool)的 buffer 都有
     */
    static void test1() {
        IntBuffer buffer = IntBuffer.allocate(10);
        // 写入 buffer
        for (int i = 0; i < 3; i++) {
            int randomNumber = new SecureRandom().nextInt(20);
            buffer.put(randomNumber);
        }
        // 翻转,让 buffer 拥有读的能力
        buffer.flip();
        while (buffer.hasRemaining()) {
            System.out.println(buffer.get());
        }
        buffer.flip();
        for (int i = 0; i < 4; i++) {
            buffer.put(1);
        }
    }

    /**
     * fileChannel
     */
    static void test2() throws Exception {
        FileInputStream inputStream = new FileInputStream("build.gradle");
        FileChannel fileChannel = inputStream.getChannel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(512);
        // 把 fileChannel 中的数据读到 byteBuffer 中
        fileChannel.read(byteBuffer);
        byteBuffer.flip();
        while (byteBuffer.remaining() > 0) {
            byte b = byteBuffer.get();
            System.out.print((char) b);
        }
        inputStream.close();
    }

    /**
     * * buf.put(magic);    // Prepend header
     * * in.read(buf);      // Read data into rest of buffer
     * * buf.flip();        // Flip buffer
     * * out.write(buf);    // Write header + data to channel
     */
    static void test3() throws Exception {
        FileOutputStream outStream = new FileOutputStream("test.txt");
        FileChannel fileChannel = outStream.getChannel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(512);
        byte[] message = "hello world".getBytes();
        // 把 fileChannel 中的数据读到 byteBuffer 中
        for (int i = 0; i < message.length; i++) {
            byteBuffer.put(message[i]);
        }
        byteBuffer.flip();
        // 写入 fileChannel
        fileChannel.write(byteBuffer);
        outStream.close();
    }

    /**
     * 读写文件
     *
     * @throws Exception
     */
    static void test4() throws Exception {
        FileInputStream intStream = new FileInputStream("test.txt");
        FileOutputStream outStream = new FileOutputStream("test1.txt");
        FileChannel fileInputChannel = intStream.getChannel();
        FileChannel fileOutputChannel = outStream.getChannel();

        ByteBuffer byteBuffer = ByteBuffer.allocate(512);
        while (true) {
            // 如果没有这句就无限循环了
            byteBuffer.clear();
            int read = fileInputChannel.read(byteBuffer);
            System.out.println("read: " + read);
            if (read == -1) {
                break;
            }
            byteBuffer.flip();
            fileOutputChannel.write(byteBuffer);
        }

        intStream.close();
        outStream.close();
    }


    public static void main(String[] args) throws Exception {
        test4();
    }
}
