package org.neo4j.extensions.spring.common;

public interface OutboundGateway {

    public void sendToRabbit(Object object);

    public void sendToActiveMq(Object object);

    public void sendToZero(Object object);

    public void sendToKafka(Object object);

    public void sendAlbumRangeToRabbit(int start, int end);

    public void sendAlbumRangeToActiveMq(int start, int end);

    public void sendBookRangeToRabbit(int start, int end);

    public void sendBookRangeToActiveMq(int start, int end);
    
    public void sendAlbumRangeToKafka(int start, int end);

    public void sendBookRangeToKafka(int start, int end);

}