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
    private Map<String, Set<Session>> subscribeMap = new HashMap<>();// 存储会话信息
    private Map<String, String> accessMap = new HashMap<>();// AK/SK鉴权


    @Override
    public MqServer addAccess(String accessKey, String accessSecretKey) {
        accessMap.put(accessKey, accessSecretKey);
        return this;
    }

    /**
     * 服务器端连接
     * @param port
     */
    @Override
    public MqServer start(int port) throws  Exception{
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
        return this;
    }

    @Override
    public MqServer stop() {
        server.stop();
        return this;
    }


    /**
     * ；连接的时候用于ak/sk鉴权
     * @param session
     * @throws IOException
     */
    @Override
    public void onOpen(Session session) throws IOException {
        super.onOpen(session);
        if (accessMap.size() > 0) {
            //如果有 ak/sk 配置，则进行鉴权
            String accessKey = session.param(MqConstants.PARAM_ACCESS_KEY);
            String accessSecretKey = session.param(MqConstants.PARAM_ACCESS_SECRET_KEY);

            if (accessKey == null || accessSecretKey == null) {
                session.close();
                return;
            }

            if (accessSecretKey.equals(accessMap.get(accessKey)) == false) {
                session.close();
                return;
            }
        }
    }

    /**
     * 订阅消息    当订阅时
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
     * 发布消息  派发消息  当派发时
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


}
