package com.ccc.folkmq.client;

/**
 * 消息客户端
 *
 * @author noear
 * @since 1.0
 */
// 客户端同时继承生产者和消费着
public interface MqClient extends MqConsumer, MqProducer {
}
