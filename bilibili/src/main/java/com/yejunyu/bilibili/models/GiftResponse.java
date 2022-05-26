package com.yejunyu.bilibili.models;

import lombok.Data;

/**
 * @author : YeJunyu
 * @description : 直播间礼物信息
 * @email : yyyejunyu@gmail.com
 * @date : 2022/5/24
 */
@Data
public class GiftResponse {

    private GiftData data;

    private String cmd = "LIVE_OPEN_PLATFORM_DM";

    @Data
    static public class GiftData {
        private long roomId;

        /**
         * 送礼 uid
         */
        private long uid;
        private String uname;
        /**
         * 头像
         */
        private String uface;
        /**
         * 送礼的 id
         */
        private long giftId;
        /**
         * 礼物名字
         */
        private String giftName;
        /**
         * 礼物数量
         */
        private int giftNum;
        /**
         * 支付金额(1000 = 1元 = 10电池),盲盒:爆出道具的价值
         */
        private long price;
        /**
         * 是否真的花钱(电池道具)
         */
        private boolean paid;
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

        private long timestamp;
        /**
         * 主播信息
         */
        private AnchorInfo anchorInfo;
    }
}
