package com.ccc.folkmq.client;

import java.io.IOException;

/**
 * 消费者
 */
public interface MqConsumer {

    /**
     * 订阅消息
     * @param topic  主题
     * @param handler 消费处理
     */
    void subscribe(String topic, MqConsumerHandler handler) throws IOException;
}
