package org.neo4j.extensions.spring.zero;

import java.io.IOException;
import java.net.ServerSocket;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.integration.Message;
import org.springframework.integration.context.IntegrationObjectSupport;
import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Context;
import org.zeromq.ZMQ.Socket;

/**
 * @author joemoore
 * @since 0.5
 */
public class ZeroMqProducerContext extends IntegrationObjectSupport {
    private static final Log LOGGER = LogFactory
            .getLog(ZeroMqProducerContext.class);
    public Context context;
    public Socket publisher;
    public int port;

    ZeroMqProducerContext() {
        try {
            ServerSocket serverSocket = new ServerSocket(0);
            port = serverSocket.getLocalPort();
            serverSocket.close();
        } catch (IOException e) {
            LOGGER.error("failed to bind server socket: " + port, e);
        }
        init(port);
    }

    ZeroMqProducerContext(int port) {
        init(port);
    }
    
    private void init (int port) {
        context = ZMQ.context(1);
        publisher = context.socket(ZMQ.PUB);
        publisher.bind("tcp://localhost:" + port);
    }

    public void send(final Message<?> message) throws Exception {
        publisher.sendMore(message.getHeaders().toString());
        publisher.send(message.getPayload().toString());
    }

}
