package reactor;

import java.io.IOException;
import java.net.ServerSocket;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;

/**
 * @author : YeJunyu
 * @description :
 * @email : yyyejunyu@gmail.com
 * @date : 2021/7/9
 */
public class Reactor implements Runnable{

    SelectionKey selectionKey;

    private Reactor() throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        Selector selector = Selector.open();
        ServerSocket socket = serverSocketChannel.socket();
        this.selectionKey = serverSocketChannel.register(selector,SelectionKey.OP_ACCEPT);
        selectionKey.attach(new Acceptor());
    }

    @Override
    public void run() {

    }

    static class Acceptor implements Runnable{

        @Override
        public void run() {
            try {
                
            }catch (Exception e){

            }
        }
    }
}
