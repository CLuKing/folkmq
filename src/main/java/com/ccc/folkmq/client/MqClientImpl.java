package com.ccc.folkmq.client;


import com.ccc.folkmq.MqConstants;
import org.noear.socketd.SocketD;
import org.noear.socketd.transport.core.Message;
import org.noear.socketd.transport.core.Session;
import org.noear.socketd.transport.core.entity.StringEntity;
import org.noear.socketd.transport.core.listener.SimpleListener;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 消息客户端   具体实现  消费者和生产者
 *
 */
public class MqClientImpl extends SimpleListener implements MqClient {

    private String serverUrl;// 消息连接
    private Session session; // 会话
    private Map<String,MqConsumerHandler> subscribeMap = new HashMap<>();// 主题处理的登记


    public MqClientImpl(String serverUrl) throws Exception{
        this.serverUrl = serverUrl;
        this.session = SocketD.createClient(serverUrl)
                .listen(this)
                .open();
    }


    /**
     * 订阅消息   消费者
     * @param topic  主题
     * @param handler 消费处理
     */
    @Override
    public void Subscribe(String topic, MqConsumerHandler handler) throws IOException {
        subscribeMap.put(topic, handler);

        session.send(MqConstants.MQ_EVENT_SUBSCRIBE, new StringEntity("").meta(MqConstants.MQ_META_TOPIC, topic));// 消费的元信息
    }

    /**
     * 发布消息  生产者
     * @param topic  主题
     * @param message  消息
     */
    @Override
    public void publish(String topic, String message) throws IOException{
        session.send(MqConstants.MQ_EVENT_PUBLISH, new StringEntity(message).meta(MqConstants.MQ_META_TOPIC, topic));// 消费的元信息

    }

    /**
     * 接受回来的消息   通知回来
     * @param session
     * @param message
     * @throws IOException
     */
    @Override
    public void onMessage(Session session, Message message) throws IOException {
        String topic = message.meta(MqConstants.MQ_META_TOPIC);// 获取主题
        MqConsumerHandler handler = subscribeMap.get(topic);// 消费处理器
        if (handler != null){
            handler.handler(topic, message.dataAsString());// 消息进行处理
        }
    }
}
