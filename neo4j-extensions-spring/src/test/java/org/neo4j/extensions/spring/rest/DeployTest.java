package org.neo4j.extensions.spring.rest;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Arrays;

import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.apache.cxf.jaxrs.provider.json.JSONProvider;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.neo4j.extensions.client.UserClient;
import org.neo4j.extensions.spring.domain.FriendResult;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.kernel.GraphDatabaseAPI;
import org.neo4j.server.CommunityNeoServer;
import org.neo4j.server.helpers.CommunityServerBuilder;
/**
 * Test server deploys successfully.
 *
 * @author joemoore
 * @version 0.1.0
 * @since 0.1.0
 */
@SuppressWarnings("deprecation")
public class DeployTest {

    private GraphDatabaseAPI db;
    private CommunityNeoServer server;

    @Before
    public void before() throws IOException {
        ServerSocket serverSocket = new ServerSocket(0);
        server = CommunityServerBuilder
                .server()
                .onPort(serverSocket.getLocalPort())
                .withDefaultDatabaseTuning()
                .withThirdPartyJaxRsPackage("org.neo4j.extensions.spring", "/extensions-spring")
                .build();
        server.start();
        db = server.getDatabase().getGraph();
        serverSocket.close();
    }

    @After
    public void after() {
        server.stop();
    }

    @Test
    public void shouldCreateUser() {
        Response response = getClient().create(false);
        FriendResult result = response.readEntity(FriendResult.class);

        Assert.assertNotNull(result.getFriends());
        Assert.assertNotNull(result.getUser());
        Transaction tx = db.beginTx();
        Node node = db.getNodeById(result.getUser().getId());
        Assert.assertNotNull(node.getProperty("createdTime").toString());
        Assert.assertEquals(node.getProperty("createdTime").toString(), result.getUser().getCreatedTime().toString());
        tx.success();
        tx.close();
    }

    private UserClient getClient() {
        JSONProvider jsonP = new JSONProvider();
        jsonP.setSupportUnwrapped(true);
        jsonP.setDropRootElement(true);
        return JAXRSClientFactory.create(server.baseUri().toString() + "extensions-spring", UserClient.class, Arrays.asList(new JSONProvider[] {jsonP}));
    }

}
