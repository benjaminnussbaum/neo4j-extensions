package org.neo4j.extensions.spring.common;

import org.neo4j.extensions.spring.domain.User;


public interface OutboundGateway {
    
    public void sendToRabbit(User user);
}