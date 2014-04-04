package org.neo4j.extensions.client;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * The User client interface.
 *
 * @author joemoore
 * @version 0.1.0
 * @since 0.1.0
 */
@Path("/user")
public interface UserClient {


    /**
     * @return Status 201 on success.
     */
    @POST
    @Path("/create")
    @Produces({
            MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML
    })
    public Response create(@QueryParam("indexingOn") @DefaultValue("true") Boolean indexingOn);

}
