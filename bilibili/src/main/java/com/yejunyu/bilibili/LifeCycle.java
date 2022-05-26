package com.yejunyu.bilibili;

/**
 * @author : YeJunyu
 * @description :
 * @email : yyyejunyu@gmail.com
 * @date : 2022/5/19
 */
public interface LifeCycle {
    /**
     * 容器初始化
     */
    void init();

    /**
     * 容器启动
     */
    void start();

    /**
     * 容器关闭
     */
    void shutdown();
}
