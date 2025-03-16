package com.ccc.folkmq.client;

import java.io.IOException;

/**
 * 生产者
 */
public interface MqProducer {

    /**
     * 发送消息
     * @param topic  主题
     * @param message  消息
     */
    void publish(String topic, String message) throws IOException;
}
