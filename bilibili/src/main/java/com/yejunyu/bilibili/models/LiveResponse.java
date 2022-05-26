package com.yejunyu.bilibili.models;

import lombok.Data;

/**
 * @author : YeJunyu
 * @description : 直播间弹幕信息返回
 * @email : yyyejunyu@gmail.com
 * @date : 2022/5/24
 */
@Data
public class LiveResponse {

    private SmsData data;

    private String cmd = "LIVE_OPEN_PLATFORM_DM";

    @Data
    static public class SmsData {
        /**
         * 粉丝勋章等级
         */
        private long fansMedalLevel;
        /**
         * 佩戴的粉丝勋章佩戴状态
         */
        public String fansMedalName;
        /**
         * 佩戴的粉丝勋章佩戴状态
         */
        private boolean fansMedalWearingStatus;
        /**
         * 大航海等级
         */
        private long guardLevel;

        private String msg;

        private long timestamp;

        private long uid;

        private String uname;

        private String uface;

        private String msgId;

        private long roomId;
    }
}
