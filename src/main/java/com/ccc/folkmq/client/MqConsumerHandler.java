package com.ccc.folkmq.client;


/**
 * 消费者处理
 *
 * @author noear
 * @since 1.0
 */
public interface MqConsumerHandler {
    /**
     * 处理消息
     * @param topic   主题
     * @param message  消息内容
     */
    void handler(String topic, String message);
}
