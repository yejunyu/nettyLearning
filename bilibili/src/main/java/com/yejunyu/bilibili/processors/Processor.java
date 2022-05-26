package com.yejunyu.bilibili.processors;

import com.yejunyu.bilibili.context.WbRequestWrapper;

/**
 * @author : YeJunyu
 * @description : 执行器
 * @email : yyyejunyu@gmail.com
 * @date : 2022/5/19
 */
@FunctionalInterface
public interface Processor {
    /**
     * 执行器执行
     *
     * @param requestWrapper
     */
    void process(WbRequestWrapper requestWrapper);


}
