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
import org.neo4j.extensions.client.SearchIndexClient;
import org.neo4j.extensions.client.UserClient;
import org.neo4j.extensions.spring.domain.FriendResult;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.server.CommunityNeoServer;
import org.neo4j.server.helpers.CommunityServerBuilder;
import org.springframework.data.neo4j.server.ProvidedClassPathXmlApplicationContext;

import com.mediahound.graph.test.domain.BookDataOnDemand;
/**
 * Test server deploys successfully.
 *
 * @author joemoore
 * @version 0.1.0
 * @since 0.1.0
 */
public class DeployTest {

    private GraphDatabaseService db;
    private CommunityNeoServer server;
    
    private ProvidedClassPathXmlApplicationContext ctx;
    
    private BookDataOnDemand bookData;

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
        ctx = new ProvidedClassPathXmlApplicationContext( db, new String[]{
                "META-INF/spring/test-springContext.xml"
        } );
        bookData = ctx.getBean("bookDataOnDemand", BookDataOnDemand.class);
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
    
    @Test
    public void shouldIndexBooks() {
        bookData.init();
        bookData.getManyNewPersisted();
        Response response = getIndexClient().indexBooksZero();
    }

    private UserClient getClient() {
        JSONProvider jsonP = new JSONProvider();
        jsonP.setSupportUnwrapped(true);
        jsonP.setDropRootElement(true);
        return JAXRSClientFactory.create(server.baseUri().toString() + "extensions-spring", UserClient.class, Arrays.asList(new JSONProvider[] {jsonP}));
    }
    
    private SearchIndexClient getIndexClient() {
        JSONProvider jsonP = new JSONProvider();
        jsonP.setSupportUnwrapped(true);
        jsonP.setDropRootElement(true);
        return JAXRSClientFactory.create(server.baseUri().toString() + "extensions-spring", SearchIndexClient.class, Arrays.asList(new JSONProvider[] {jsonP}));
    }

}
