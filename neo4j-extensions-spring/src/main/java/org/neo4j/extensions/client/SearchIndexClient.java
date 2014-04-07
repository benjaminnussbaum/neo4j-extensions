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
    @Path("/zero/users")
    Response indexUsersZero() ;
    
    /**
     * @return Status 200 on success.
     */
    @POST
    @Path("/kafka/users")
    Response indexUsersKafka() ;
    
    /**
     * @return Status 200 on success.
     */
    @POST
    @Path("/activemq/users")
    Response indexUsersActiveMq();
    /**
     * @return Status 200 on success.
     */
    @POST
    @Path("/rabbitmq/users")
    Response indexUsersRabbitMq();
    
    
}
