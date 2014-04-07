package org.neo4j.extensions.spring.common;

import java.net.ServerSocket;

import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Context;
import org.zeromq.ZMQ.Socket;

public class JeroMQPublisher {
    
    public Context context;
    
    public Socket publisher;
    
    public int socket;
    
    public void init() throws Exception {
        // Prepare our context and publisher
        context = ZMQ.context(1);
        publisher = context.socket(ZMQ.PUB);
        ServerSocket serverSocket = new ServerSocket(0);
        socket = serverSocket.getLocalPort();
        publisher.bind("tcp://*:" + socket);
        serverSocket.close();
      }
   
      public void cleanUp() throws Exception {
          publisher.close();
          context.term();
      }

      public int getPort() {
          return socket;
      }
      
      public void sendEnvelopeMessage(String envelope, String message) {
          publisher.sendMore(envelope);
          publisher.send(message);
      }
      
}