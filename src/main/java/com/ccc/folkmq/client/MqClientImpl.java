package com.ccc.folkmq.client;


import com.ccc.folkmq.MqConstants;
import org.noear.socketd.SocketD;
import org.noear.socketd.transport.core.Message;
import org.noear.socketd.transport.core.Session;
import org.noear.socketd.transport.core.entity.StringEntity;
import org.noear.socketd.transport.core.listener.BuilderListener;
import org.noear.socketd.transport.core.listener.SimpleListener;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * 消息客户端   具体实现  消费者和生产者
 *
 */
public class MqClientImpl extends BuilderListener implements MqClient {

    private String serverUrl;// 消息连接
    private Session session; // 会话
    private Map<String,MqConsumerHandler> subscribeMap = new HashMap<>();// 主题处理的登记


    public MqClientImpl(String serverUrl) throws Exception{
//        this.serverUrl = serverUrl;// 修改一下请求头昵称
//        this.serverUrl = serverUrl.replace("folkmq://", "sd:tcp://");// 修改一下请求头昵称
        this.session = SocketD.createClient(serverUrl)
                .listen(this)
                .open();

        // 事件派发
        on(MqConstants.MQ_EVENT_DISTRIBUTE,(s,m)->{
            String topic = m.meta(MqConstants.MQ_META_TOPIC);
            OnDistribute(topic,m.dataAsString());
        });
    }


    /**
     * 订阅消息   消费者
     * @param topic  主题
     * @param handler 消费处理
     */
    @Override
    public CompletableFuture<?> subscribe(String topic, MqConsumerHandler handler) throws IOException {
        subscribeMap.put(topic, handler);

        //改为异步的方式，是否等待由用户选择
        CompletableFuture<?> future = new CompletableFuture<>();
        // 请求接口的订阅接口  有个回调值
        session.sendAndSubscribe(MqConstants.MQ_EVENT_SUBSCRIBE, new StringEntity("").meta(MqConstants.MQ_META_TOPIC, topic),(r)->{
            future.complete(null);
        });// 消费的元信息
        return future;
    }

    /**
     * 发布消息  生产者
     * @param topic  主题
     * @param message  消息
     */
    @Override
    public CompletableFuture<?> publish(String topic, String message) throws IOException{

        //改为异步的方式，是否等待由用户选择
        CompletableFuture<?> future = new CompletableFuture<>();
        // 请求接口的订阅接口  有个回调值
        session.sendAndSubscribe(MqConstants.MQ_EVENT_PUBLISH, new StringEntity(message).meta(MqConstants.MQ_META_TOPIC, topic),(r)->{
                    future.complete(null);
        });// 消费的元信息
        return future;

    }

    /**
     * 接受回来的消息   通知回来  当派发时
     * @param topic
     * @param message
     * @throws IOException
     */

    public void OnDistribute(String topic, String message) throws IOException {
        MqConsumerHandler handler = subscribeMap.get(topic);// 消费处理器
        System.out.printf("1111111");
        if (handler != null){
            handler.handler(topic, message);// 消息进行处理
        }
    }
}
