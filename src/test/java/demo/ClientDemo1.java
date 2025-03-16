package demo;

import com.ccc.folkmq.client.MqClient;
import com.ccc.folkmq.client.MqClientImpl;

public class ClientDemo1 {

    public static void main(String[] args) throws Exception {
        //客户端
//        MqClient client = new MqClientImpl("folkmq://127.0.0.1:9393&ak=1?sk=2");// 可能是服务器启动原因，端口有问题，不做修改了
        MqClient client = new MqClientImpl("sd:tcp://127.0.0.1:9393?ak=1&sk=2");

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
