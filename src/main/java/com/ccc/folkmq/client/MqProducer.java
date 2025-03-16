package com.ccc.folkmq.client;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

/**
 * 生产者
 */
public interface MqProducer {

    /**
     * 发送消息
     * @param topic  主题
     * @param message  消息
     */
    CompletableFuture<?> publish(String topic, String message) throws IOException;//换为响应式接口
}
