package org.neo4j.extensions.spring.rest;

import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.neo4j.extensions.client.SearchIndexClient;
import org.neo4j.extensions.spring.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Search Indexing controller with zeromq.
 * 
 * @author joemoore
 * @version 0.1.0
 * @since 0.1.0
 */
@Path("/search/index")
public class SearchIndexController implements SearchIndexClient {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(SearchIndexController.class);

    @Context
    private UserRepository userRepository;

    public Response indexUsersZero() {
        LOGGER.info(String
                .format("User search index start"));
        long startTimeTx = System.currentTimeMillis();

        // capture times
        long successTimeTx = System.currentTimeMillis();
        long processTimeTx = successTimeTx - startTimeTx;

        // log details
        LOGGER.debug(String.format(
                "SearchIndexZeroController: TX: processTime=%dms",
                processTimeTx));
        return Response.ok().build();
    }
    
    public Response indexUsersKafka() {
        LOGGER.info(String
                .format("User search index start"));
        long startTimeTx = System.currentTimeMillis();

        // capture times
        long successTimeTx = System.currentTimeMillis();
        long processTimeTx = successTimeTx - startTimeTx;

        // log details
        LOGGER.debug(String.format(
                "SearchIndexZeroController: TX: processTime=%dms",
                processTimeTx));
        return Response.ok().build();
    }

}
