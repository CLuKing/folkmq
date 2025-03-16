package demo;

import com.ccc.folkmq.client.MqClient;
import com.ccc.folkmq.client.MqClientImpl;

public class ClientDemo4 {

    public static void main(String[] args) throws Exception {
        //客户端
        MqClient client = new MqClientImpl("sd:tcp://127.0.0.1:9393?ak=1&sk=12");// 错误秘钥

        //订阅
        client.subscribe("demo2", ((topic, message) ->  {
            System.out.println("ClientDemo3::" +topic+'-' +message);
        }));



        //延迟五秒
//        client.publish("demo3", "hi-d", new Date(System.currentTimeMillis() + 5000));
    }
}
