package com.yejunyu.bilibili;

/**
 * @author : YeJunyu
 * @description :
 * @email : yyyejunyu@gmail.com
 * @date : 2022/5/19
 */
public class BootMain {

    public static void main(String[] args) {
        final BootContainer bootContainer = new BootContainer();
        bootContainer.start();
        Runtime.getRuntime().addShutdownHook(new Thread(bootContainer::shutdown));
    }
}
