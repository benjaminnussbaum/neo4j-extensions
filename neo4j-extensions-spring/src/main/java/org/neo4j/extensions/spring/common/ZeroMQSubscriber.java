package org.neo4j.extensions.spring.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.support.MessageBuilder;
import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Context;
import org.zeromq.ZMQ.Socket;

@Configurable
public class ZeroMQSubscriber implements Runnable {
    
    @Autowired
    @Qualifier("zeroMqQueueChannel")
    private QueueChannel zeroMqQueueChannel;

    public Context context;

    public Socket subscriber;

    public int port;
    
    public boolean stopped = false;

    public void init() throws Exception {
        // Prepare our context and subscriber
        context = ZMQ.context(1);
        subscriber = context.socket(ZMQ.SUB);
//        subscriber.setReceiveTimeOut(10);
        subscriber.connect("tcp://localhost:" + port);
        subscriber.subscribe(new byte[] {});
    }

    public void cleanUp() throws Exception {
        stopped = true;
        Thread.sleep(100L);
        subscriber.close();
        context.term();
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
    
    public void run() {
        while (!Thread.currentThread ().isInterrupted()) {
            String headerStr = subscriber.recvStr();
            String bodyStr = subscriber.recvStr();
            if (bodyStr != null && !bodyStr.isEmpty()) {
                zeroMqQueueChannel.send(MessageBuilder.withPayload(bodyStr).build());
            }
        }
    }

}