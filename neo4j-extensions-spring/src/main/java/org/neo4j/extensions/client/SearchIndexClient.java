package org.neo4j.extensions.client;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

/**
 * The Search Indexing controller.
 *
 * @author joemoore
 * @version 0.1.0
 * @since 0.1.0
 */
@Path("/search/index")
public interface SearchIndexClient {


    /**
     * @return Status 200 on success.
     */
    @POST
    @Path("/zero/books")
    Response indexBooksZero() ;
    
    /**
     * @return Status 200 on success.
     */
    @POST
    @Path("/kafka/books")
    Response indexBooksKafka() ;
    
    /**
     * @return Status 200 on success.
     */
    @POST
    @Path("/activemq/books")
    Response indexBooksActiveMq();
    /**
     * @return Status 200 on success.
     */
    @POST
    @Path("/rabbitmq/books")
    Response indexBooksRabbitMq();
    
    
}
