package com.ccc.folkmq.server;

import com.ccc.folkmq.MqConstants;
import org.noear.socketd.SocketD;
import org.noear.socketd.transport.core.Message;
import org.noear.socketd.transport.core.Session;
import org.noear.socketd.transport.core.listener.BuilderListener;
import org.noear.socketd.transport.server.Server;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class MqServerImpl extends BuilderListener implements MqServer{

    private Server server;
    private Map<String, Set<Session>> subscribeMap = new HashMap<>();


    /**
     * 服务器端连接
     * @param port
     */
    @Override
    public void start(int port) throws  Exception{
        server = SocketD.createServer("sd:tcp")
                .config(c->c.port(port))
                .listen(this)
                .start();

        // 接受订阅指令
        on(MqConstants.MQ_EVENT_SUBSCRIBE,(s,m)->{
            String topic = m.meta(MqConstants.MQ_META_TOPIC);
            onSubscribe(topic, s);
        });

        // 接受发布指令
        on(MqConstants.MQ_EVENT_PUBLISH,(s,m)->{
            String topic = m.meta(MqConstants.MQ_META_TOPIC);
            onPublish(topic, m);
        });
    }

    /**
     * 订阅消息
     * @param topic
     * @param session
     */
    private synchronized void onSubscribe(String topic, Session session){
        Set<Session> sessions = subscribeMap.get(topic);
        if (sessions == null){
            sessions = new LinkedHashSet<>();
            subscribeMap.put(topic, sessions);
        }
        sessions.add(session);
    }


    /**
     * 发布消息  派发消息
     * @param topic
     * @param message
     */
    private  void onPublish(String topic, Message message) throws IOException {
        // 取出所有订阅的会话
        Set<Session> sessions = subscribeMap.get(topic);
        if (sessions != null){
            for(Session s : sessions){
                s.send(MqConstants.MQ_EVENT_DISTRIBUTE,message);
            }
        }
    }

    @Override
    public void stop() {
        server.stop();
    }
}
