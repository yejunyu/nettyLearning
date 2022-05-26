package com.yejunyu.bilibili.processors;

import com.yejunyu.bilibili.context.WbRequestWrapper;

/**
 * @author : YeJunyu
 * @description :
 * @email : yyyejunyu@gmail.com
 * @date : 2022/5/25
 */
public class FlusherProcessor implements Processor{

    Processor processor;

    public FlusherProcessor(Processor processor) {
        this.processor = processor;
    }

    @Override
    public void process(WbRequestWrapper requestWrapper) {
        this.processor.process(requestWrapper);
    }

}
