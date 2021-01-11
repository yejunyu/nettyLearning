package zerocopy;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

/**
 * @author yejunyu
 * @date 2020/12/12
 * @desc
 */
public class OldClient {

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 9999);
        String fileName = "/Users/yejunyu/Desktop/面向对象葵花宝典++思想、技巧与实践.pdf";
        InputStream inputStream = new FileInputStream(fileName);
        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
        byte[] bytes = new byte[4096];
        long readCount;
        long total = 0;
        long startTime = System.currentTimeMillis();

        while ((readCount = inputStream.read(bytes)) >= 0) {
            total += readCount;
            dataOutputStream.write(bytes);
        }
        System.out.println("发送字节数: " + total + " , 耗时 :" + (System.currentTimeMillis() - startTime));
        dataOutputStream.close();
        socket.close();
        inputStream.close();
    }
}
