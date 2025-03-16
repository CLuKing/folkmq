package demo;

import com.ccc.folkmq.client.MqClient;
import com.ccc.folkmq.client.MqClientImpl;

public class ClientDemo {

    public static void main(String[] args) throws Exception {
        //客户端
        MqClient client = new MqClientImpl("sd:tcp://127.0.0.1:9393");

        //订阅
        client.subscribe("demo", ((topic, message) ->  {
            System.out.println("ClientDemo1::" + message);
        }));

        //发布
        for (int i = 0; i < 10; i++) {
//            Thread.sleep(100);
            client.publish("demo", "hi-" + i);
//            System.out.println("111");
//            client.publish("demo2", "hi-" + i);
        }

        //延迟五秒
//        client.publish("demo3", "hi-d", new Date(System.currentTimeMillis() + 5000));
    }
}
