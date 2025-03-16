package demo;

import com.ccc.folkmq.server.MqServer;
import com.ccc.folkmq.server.MqServerImpl;

public class ServerDemo {
    public static void main(String[] args) throws Exception{
        MqServerImpl server = new MqServerImpl();
        server.start(9393);
    }
}
