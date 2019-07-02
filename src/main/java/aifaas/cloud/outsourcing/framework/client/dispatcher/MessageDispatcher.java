package aifaas.cloud.outsourcing.framework.client.dispatcher;

import aifaas.cloud.outsourcing.common.utils.spring.SpringUtils;
import aifaas.cloud.outsourcing.framework.client.domain.Request;
import aifaas.cloud.outsourcing.framework.client.domain.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.mina.core.session.IoSession;

import java.net.InetSocketAddress;
import java.net.URLEncoder;
@Slf4j
public class MessageDispatcher {


    public void dispatcher(IoSession session, Object req){
        Response response = null;
        Request request = (Request) req;
        log.info("客户端请求："+request);
        try {
            IParseMessageCommand command = (IParseMessageCommand) SpringUtils.getBean(request.getMessageType());
            request.getCommon().setClient_ip(((InetSocketAddress)session.getRemoteAddress()).getAddress().getHostAddress());
            response = command.parseMessage(request);
        } catch (Exception e) {
            log.error("服务器异常:"+request.getMessageType()+"-"+e.getMessage());
            response = Response.response(request.getCommandType(),-1,"服务器异常");
        } finally {
            try {
                String res = response.writeBackStr();
                log.info("服务器返回："+res);
                session.write(URLEncoder.encode(res,"utf-8"));
            }catch (Exception e){
                log.error("服务器回复客户端失败："+e.getMessage());
            }

        }
    }
}
