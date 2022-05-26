package com.yejunyu.bilibili.server;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.digest.HMac;
import cn.hutool.crypto.digest.HmacAlgorithm;
import cn.hutool.crypto.digest.MD5;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.yejunyu.bilibili.BConfig;
import com.yejunyu.bilibili.models.AuthResponse;
import lombok.Data;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author : YeJunyu
 * @description : 短链,用于获取 b 站直播长链地址
 * @email : yyyejunyu@gmail.com
 * @date : 2022/5/18
 */
@Data
public class AuthClient {

    private String wsUrl;

    private String ip;

    private int port;

    private String authBody;

    public AuthClient() {
        String postUrl = String.format("https://%s/v1/common/websocketInfo", BConfig.host);
        String param = String.format("{\"room_id\":%s}", BConfig.roomId);
        final String md5Data = MD5.create().digestHex(param);
        // 签名
        Map<String, String> headerMap = new LinkedHashMap<>(16);
        final int l = (int) (System.currentTimeMillis() / 1000);
        String nonce = RandomUtil.randomInt(1, 100000) + l + "";
        headerMap.put("x-bili-accesskeyid", BConfig.key);
        headerMap.put("x-bili-content-md5", md5Data);
        headerMap.put("x-bili-signature-method", "HMAC-SHA256");
        headerMap.put("x-bili-signature-nonce", nonce);
        headerMap.put("x-bili-signature-version", "1.0");
        headerMap.put("x-bili-timestamp", String.valueOf(l));
        List<String> head = headerMap.entrySet().stream().map(k -> k.getKey() + ":" + k.getValue()).collect(Collectors.toList());
        final String headerStr = String.join("\n", head);
        final String signature = new HMac(HmacAlgorithm.HmacSHA256, BConfig.secret.getBytes()).digestHex(headerStr.getBytes());
        headerMap.put("Authorization", signature);
        headerMap.put("Content-Type", "application/json");
        headerMap.put("Accept", "application/json");
        final HttpResponse response = HttpUtil.createPost(postUrl).headerMap(headerMap, true).body(param.getBytes()).execute();
        System.out.println("鉴权返回结果: " + response.toString());
        final AuthResponse authResponse = JSON.parseObject(response.body(), AuthResponse.class);
        this.wsUrl = parseGetWsUrl(authResponse);
        this.authBody = authResponse.getData().getAuthBody();
        this.ip = authResponse.getData().getHost().get(0);
        this.port = authResponse.getData().getWsPort().get(0);
    }

    private String parseGetWsUrl(AuthResponse response) {
        return "ws://" + response.getData().getHost().get(0) + ":" + response.getData().getWsPort().get(0) + "/sub";
    }

}
