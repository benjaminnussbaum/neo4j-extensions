package org.neo4j.extensions.spring.common;




public interface OutboundGateway {
    
    public void sendToRabbit(Object object);
    
    public void sendToActiveMq(Object object);
    
    public void sendToZero(Object object);
    
    public void sendToKafka(Object object);
    
    public void sendBookRangeToRabbit(int start, int end);
    
    public void sendBookRangeToActiveMq(int start, int end);
    
    
}