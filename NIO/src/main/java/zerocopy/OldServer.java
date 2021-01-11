package zerocopy;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author yejunyu
 * @date 2020/12/12
 * @desc
 */
public class OldServer {

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(9999);
        while (true){
            Socket socket = serverSocket.accept();
            DataInputStream inputStream = new DataInputStream(socket.getInputStream());
            try {
                byte[] bytes = new byte[4096];
                while (true){
                    int read = inputStream.read(bytes, 0, bytes.length);
                    if (read<0){
                        break;
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
