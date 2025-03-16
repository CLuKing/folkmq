package com.ccc.folkmq.server;


/**
 * 服务端
 */
public interface MqServer {
    void start(int port);
    void stop();
}
