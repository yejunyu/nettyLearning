package bioServer;

import java.io.IOException;
import java.net.Socket;

/**
 * @author : YeJunyu
 * @description :
 * @email : yyyejunyu@gmail.com
 * @date : 2020/10/15
 */
public class Client {
    private static final int PORT = 8080;
    private static final String HOST = "127.0.0.1";
    private static final int SLEEP = 5000;

    public static void main(String[] args) throws IOException {
        final Socket  socket = new Socket(HOST,PORT);
        new Thread(() -> {
            System.out.println("客户端启动~");
            while (true){
                String message = "abc test";
                try {
                    socket.getOutputStream().write(message.getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                sleep();
            }
        }).start();
    }

    private static void sleep() {
        try {
            Thread.sleep(SLEEP);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
