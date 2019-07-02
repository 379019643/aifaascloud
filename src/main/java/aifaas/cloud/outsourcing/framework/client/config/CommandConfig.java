package aifaas.cloud.outsourcing.framework.client.config;

import aifaas.cloud.outsourcing.framework.client.command.UserLoginComm;

import java.util.HashMap;
import java.util.Map;

public class CommandConfig {
    private static final Map<Integer,Class> map = new HashMap<Integer, Class>();
    private CommandConfig(){}
    static {
        map.put(1, UserLoginComm.class);
    }

    public static Class getType(Integer in){
        return map.get(in);
    }
}
