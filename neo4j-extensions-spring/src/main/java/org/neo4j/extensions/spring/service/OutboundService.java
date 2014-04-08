package org.neo4j.extensions.spring.service;

import org.neo4j.extensions.spring.common.JeroMQPublisher;
import org.neo4j.extensions.spring.common.OutboundGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mediahound.graph.service.BookService;

public class OutboundService {
    @Autowired
    private BookService bookService;
    
    @Autowired
    private OutboundGateway outboundGateway;
    
    @Autowired
    JeroMQPublisher jeroMQPublisher;
    
    
}
