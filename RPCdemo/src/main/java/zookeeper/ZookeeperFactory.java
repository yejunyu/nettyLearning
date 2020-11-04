package zookeeper;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.net.InetAddress;

/**
 * @author: yejunyu
 * date: 2018/11/21
 */
public class ZookeeperFactory {

    public static CuratorFramework client;

    public static CuratorFramework create() {
        if (client == null) {
            RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
            client = CuratorFrameworkFactory.newClient("localhost:2181", retryPolicy);
            client.start();
        }
        return client;
    }

    public static void main(String[] args) throws Exception {
        CuratorFramework client = create();
        client.create().forPath("/netty");
        System.out.println(InetAddress.getLocalHost().getHostAddress());
        byte[] bytes = client.getData().forPath("/netty");
        System.out.println(new String(bytes));
    }
}
