package com.ccc.folkmq.server;


/**
 * 服务端
 */
public interface MqServer {

    // 添加AK/SK鉴权
    MqServer addAccess(String accessKey, String accessSecretKey);
    MqServer start(int port) throws Exception;
    MqServer stop();
}
