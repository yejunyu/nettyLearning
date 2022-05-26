package com.yejunyu.bilibili.models;

import lombok.Data;

import java.util.List;

/**
 * @author : YeJunyu
 * @description : 请求 ws 长连接地址的返回值
 * @email : yyyejunyu@gmail.com
 * @date : 2022/5/18
 */
@Data
public class AuthResponse {
    private int code;

    private String message;

    private String requestId;

    private BData data;

    @Data
    public static class BData {
        private String authBody;

        private List<String> host;

        private List<String> ip;

        private List<Integer> tcpPort;

        private List<Integer> wsPort;

        private List<Integer> wssPort;
    }

    @Data
    public static class AuthBody {
        private int roomid;
        private int protover;
        private int uid;
        private String key;
        private String group;
    }
}
