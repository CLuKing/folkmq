package demo;

import com.ccc.folkmq.server.MqServer;
import com.ccc.folkmq.server.MqServerImpl;

public class ServerDemo {
    public static void main(String[] args) throws Exception{
        MqServer server =  new MqServerImpl()
                .addAccess("1","2")
                .start(9393);
    }
}
