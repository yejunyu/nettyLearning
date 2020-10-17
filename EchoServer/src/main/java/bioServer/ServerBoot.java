package bioServer;

/**
 * @author : YeJunyu
 * @description :
 * @email : yyyejunyu@gmail.com
 * @date : 2020/10/15
 */
public class ServerBoot {

    private static final int PORT = 8080;

    public static void main(String[] args) {
        Server server = new Server(PORT);
        server.start();
    }
}
