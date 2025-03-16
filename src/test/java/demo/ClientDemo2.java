package demo;

import com.ccc.folkmq.client.MqClient;
import com.ccc.folkmq.client.MqClientImpl;

public class ClientDemo2 {

    public static void main(String[] args) throws Exception {
        //客户端
        MqClient client = new MqClientImpl("sd:tcp://127.0.0.1:9393?ak=1&sk=2");

        //订阅
        client.subscribe("demo2", ((topic, message) ->  {
            System.out.println("ClientDemo2::" +topic+'-' +message);
        }));



        //延迟五秒
//        client.publish("demo3", "hi-d", new Date(System.currentTimeMillis() + 5000));
    }
}
