package aifaas.cloud.outsourcing.framework.client.dispatcher;

import aifaas.cloud.outsourcing.framework.client.domain.Request;
import aifaas.cloud.outsourcing.framework.client.domain.Response;

public interface IParseMessageCommand {

    public Response parseMessage(Request request);
}
