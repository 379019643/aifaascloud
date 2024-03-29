package aifaas.cloud.outsourcing.framework.client.command;

import aifaas.cloud.outsourcing.framework.client.dispatcher.IParseMessageCommand;
import aifaas.cloud.outsourcing.framework.client.domain.Request;
import aifaas.cloud.outsourcing.framework.client.domain.ResComm;
import aifaas.cloud.outsourcing.framework.client.domain.Response;
import org.springframework.stereotype.Service;

@Service
public class UserLoginComm implements IParseMessageCommand {

    @Override
    public Response parseMessage(Request request) {
        System.out.println("处理请求中......");
        ResComm comm = new ResComm(1,"成功");
        Response response = new Response(request.getCommandType()).setCommon(comm);
        response.getDetails().put("a","收到");
        return response;
    }
}
