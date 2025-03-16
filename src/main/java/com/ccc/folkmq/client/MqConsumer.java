package com.ccc.folkmq.client;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

/**
 * 消费者
 */
public interface MqConsumer {

    /**
     * 订阅消息
     * @param topic  主题
     * @param handler 消费处理
     */
    CompletableFuture<?> subscribe(String topic, MqConsumerHandler handler) throws IOException;
}
