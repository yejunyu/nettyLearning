package com.yejunyu;

import com.alibaba.fastjson.JSON;
import com.yejunyu.bilibili.models.AuthResponse;

/**
 * @author : YeJunyu
 * @description :
 * @email : yyyejunyu@gmail.com
 * @date : 2022/5/18
 */
public class A {

    public static void main(String[] args) {
        // auth 返回值
        String json = "{\"code\":0,\"message\":\"0\",\"request_id\":\"1526887129460092928\",\"data\":{\"authBody\":\"{\\\"roomid\\\":8084101,\\\"protover\\\":2,\\\"uid\\\":2444669278,\\\"key\\\":\\\"jEteuOHJFi9NYoSYTJ4FemMvHU7M0cvHP8a3oaXyq2W1OQ3FpdRz-63gSk8Zs5PO_AnKJ8-rLnJG2vqIUe5Y1SPVf3NWBl6DbrDYUgsIMu_1carkb0qAxTajnloF4xz_T_fJi-_c5DnIH2G4mqNZ0vb9hOk=\\\",\\\"group\\\":\\\"open\\\"}\",\"host\":[\"tx-sh-live-comet-04.chat.bilibili.com\",\"tx-bj-live-comet-05.chat.bilibili.com\",\"broadcastlv.chat.bilibili.com\"],\"ip\":[\"49.235.252.237\",\"192.144.229.127\",\"broadcastlv.chat.bilibili.com\"],\"tcp_port\":[2243,80],\"ws_port\":[2244],\"wss_port\":[443]}}";
        final AuthResponse authResponse = JSON.parseObject(json, AuthResponse.class);
        System.out.println(authResponse.getData());
        // 进房返回值
        String json1 = "{\"data\":{\"fans_medal_level\":0,\"fans_medal_name\":\"\",\"fans_medal_wearing_status\":false,\"guard_level\":0,\"msg\":\"11\",\"timestamp\":1652883307,\"uid\":3031858,\"uname\":\"小鸡变鸟\",\"uface\":\"http://i1.hdslb.com/bfs/face/5e694b2eba6336a505062ae91b17ec0ecda15edc.jpg\",\"msg_id\":\"c3300c89-f4a4-4e75-934e-764fbeb88f43\",\"room_id\":8084101},\"cmd\":\"LIVE_OPEN_PLATFORM_DM\"}";
    }
}
