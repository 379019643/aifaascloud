package aifaas.cloud.outsourcing.framework.client.handler;

import aifaas.cloud.outsourcing.framework.client.dispatcher.MessageDispatcher;
import lombok.extern.slf4j.Slf4j;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class ServerHandler extends IoHandlerAdapter {
    @Autowired
    public MessageDispatcher dispatcher;
    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        this.dispatcher.dispatcher(session,message);
    }

    @Override
    public void messageSent(IoSession session, Object message) throws Exception {
        super.messageSent(session, message);
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        log.error("消息处理时发生异常："+cause.getMessage());
    }
}
