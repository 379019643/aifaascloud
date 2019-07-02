package aifaas.cloud.outsourcing.framework.client.filter;

import aifaas.cloud.outsourcing.framework.client.domain.Request;
import aifaas.cloud.outsourcing.framework.client.domain.Response;
import aifaas.cloud.outsourcing.framework.client.util.HeaderUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.mina.core.filterchain.IoFilterAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.core.write.WriteRequest;

import java.net.URLEncoder;

@Slf4j
public class HeaderCheckFilter extends IoFilterAdapter {
    @Override
    public void messageReceived(NextFilter nextFilter, IoSession session, Object message) throws Exception {
        Request request = new HeaderUtil().getMessageMapFromHeader(message);
        if (request.isOK()) {
            super.messageReceived(nextFilter, session, request);
        } else {
            Response response = Response.response(request.getCommandType(),100,"数据不合法.");
            session.write(URLEncoder.encode(response.writeBackStr(),"utf-8"));
        }
    }

    @Override
    public void messageSent(NextFilter nextFilter, IoSession session, WriteRequest writeRequest) throws Exception {
        super.messageSent(nextFilter, session, writeRequest);
    }

    @Override
    public void exceptionCaught(NextFilter nextFilter, IoSession session, Throwable cause) throws Exception {
        log.error("头信息校验发生异常："+cause.getMessage());
    }

}
