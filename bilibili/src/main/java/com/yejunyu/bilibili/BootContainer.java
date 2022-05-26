package com.yejunyu.bilibili;

import com.yejunyu.bilibili.processors.CoreProcessor;
import com.yejunyu.bilibili.processors.FlusherProcessor;
import com.yejunyu.bilibili.processors.Processor;
import com.yejunyu.bilibili.server.AuthClient;
import com.yejunyu.bilibili.server.BClient;
import com.yejunyu.bilibili.server.WbServer;

/**
 * @author : YeJunyu
 * @description : 主启动容器, 对外连接 b 站,对内传递消息
 * @email : yyyejunyu@gmail.com
 * @date : 2022/5/19
 */
public class BootContainer implements LifeCycle {

    private Processor processor;
    private BClient bClient;
    private AuthClient authClient;
    private WbServer wbServer;

    public BootContainer() {
        init();
    }

    @Override
    public void init() {
        // 1. 获取 ws 长连接地址
        this.authClient = new AuthClient();
        wbServer = new WbServer();
        // 2. 建立长连接
        processor = new CoreProcessor(wbServer);
        if (BConfig.isFlusher) {
            processor = new FlusherProcessor(processor);
        }
        this.bClient = new BClient(authClient, processor);
    }

    @Override
    public void start() {
        bClient.start();
        wbServer.start();
    }

    @Override
    public void shutdown() {
        wbServer.shutdown();
        bClient.shutdown();
    }
}
