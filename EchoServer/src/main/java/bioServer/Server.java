package bioServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author : YeJunyu
 * @description :
 * @email : yyyejunyu@gmail.com
 * @date : 2020/10/15
 */
public class Server {
    private ServerSocket socket;

    private static final int PORT = 8080;

    public Server() {
        this(PORT);
    }

    public Server(int port) {
        try {
            this.socket = new ServerSocket(port);
            System.out.println("端口启动成功, port: " + port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start(){
        new Thread(() -> doStart()).start();
    }

    public void doStart(){
        while (true){
            try {
                Socket client = socket.accept();
                new ClientHandler(client).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
