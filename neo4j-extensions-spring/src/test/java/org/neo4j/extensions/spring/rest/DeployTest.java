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
import org.junit.Ignore;
import org.junit.Test;
import org.neo4j.extensions.client.SearchIndexClient;
import org.neo4j.extensions.client.UserClient;
import org.neo4j.extensions.spring.domain.FriendResult;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.server.CommunityNeoServer;
import org.neo4j.server.helpers.CommunityServerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.neo4j.server.ProvidedClassPathXmlApplicationContext;
import org.springframework.integration.Message;
import org.springframework.integration.MessageHeaders;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.support.MessageBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mediahound.graph.domain.Album;
import com.mediahound.graph.domain.Book;
import com.mediahound.graph.test.domain.AlbumDataOnDemand;
import com.mediahound.graph.test.domain.BookDataOnDemand;

import eu.codearte.fairyland.Fairy;
import eu.codearte.fairyland.producer.person.Person;

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

    private AlbumDataOnDemand albumData;

    private BookDataOnDemand bookData;

    private QueueChannel jmsQueueChannel;

    private QueueChannel amqpQueueChannel;

    private QueueChannel kafkaQueueChannel;

    private QueueChannel zeroMqQueueChannel;
    
    private DirectChannel toActiveDirectChannel;

    private DirectChannel toRabbitDirectChannel;
    
    private DirectChannel toZeroDirectChannel;

    private ObjectMapper objectMapper = new ObjectMapper();

    private static final Logger LOGGER = LoggerFactory
            .getLogger(DeployTest.class);

    @Before
    public void before() throws IOException {
        ServerSocket serverSocket = new ServerSocket(0);
        server = CommunityServerBuilder
                .server()
                .onPort(serverSocket.getLocalPort())
                .withDefaultDatabaseTuning()
                .withThirdPartyJaxRsPackage("org.neo4j.extensions.spring",
                        "/extensions-spring").build();
        server.start();
        db = server.getDatabase().getGraph();
        ctx = new ProvidedClassPathXmlApplicationContext(db,
                new String[] { "META-INF/spring/test-springContext.xml" });
        albumData = ctx.getBean("albumDataOnDemand", AlbumDataOnDemand.class);
        bookData = ctx.getBean("bookDataOnDemand", BookDataOnDemand.class);
        amqpQueueChannel = ctx.getBean("amqpQueueChannel", QueueChannel.class);
        jmsQueueChannel = ctx.getBean("jmsQueueChannel", QueueChannel.class);
        kafkaQueueChannel = ctx
                .getBean("kafkaQueueChannel", QueueChannel.class);
        zeroMqQueueChannel = ctx.getBean("zeroMqQueueChannel",
                QueueChannel.class);
        toActiveDirectChannel = ctx.getBean("toActiveDirect",
                DirectChannel.class);
        toRabbitDirectChannel = ctx.getBean("toRabbitDirect",
                DirectChannel.class);
        toZeroDirectChannel = ctx.getBean("toZeroDirect",
                DirectChannel.class);
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
        Assert.assertEquals(node.getProperty("createdTime").toString(), result
                .getUser().getCreatedTime().toString());
        tx.success();
        tx.close();
        albumData.init();
        bookData.init();
    }

    @Test
    public void shouldIndexAlbumsActiveMq() throws Exception {
        albumData.getManyNewPersisted();
        Response response = getIndexClient().indexAlbumsActiveMq();
        Message<String> message = null;
        int count = 0;
        while ((message = (Message<String>) jmsQueueChannel.receive(1000)) != null) {
            MessageHeaders headers = message.getHeaders();
            Long time = (Long) headers.get("timestamp");
            String string = message.getPayload();
            Album album = objectMapper.readValue(string, Album.class);
            album.hashCode();
            // if (time >= start && jobIds.contains(proxy.getId())) {
            // count++;
            // }
            count++;
        }
        Assert.assertTrue(count > 0);
    }

    @Test
    public void shouldIndexBooksActiveMq() throws Exception {
        bookData.getManyNewPersisted();
        Response response = getIndexClient().indexBooksActiveMq();
        Message<String> message = null;
        int count = 0;
        while ((message = (Message<String>) jmsQueueChannel.receive(1000)) != null) {
            MessageHeaders headers = message.getHeaders();
            Long time = (Long) headers.get("timestamp");
            String string = message.getPayload();
            Book book = objectMapper.readValue(string, Book.class);
            book.hashCode();
            // if (time >= start && jobIds.contains(proxy.getId())) {
            // count++;
            // }
            count++;
        }
        Assert.assertTrue(count > 0);
    }

    @Test
    public void shouldIndexAlbumsRabbitMq() throws Exception {
        albumData.getManyNewPersisted();
        Response response = getIndexClient().indexAlbumsRabbitMq();
        Message<Album> message = null;
        int count = 0;
        while ((message = (Message<Album>) amqpQueueChannel.receive(1000)) != null) {
            MessageHeaders headers = message.getHeaders();
            Long time = (Long) headers.get("timestamp");
            Album album = message.getPayload();
            album.hashCode();
            // if (time >= start && jobIds.contains(proxy.getId())) {
            // count++;
            // }
            count++;
        }
        Assert.assertTrue(count > 0);
    }

    @Test
    public void shouldIndexBooksRabbitMq() throws Exception {
        bookData.getManyNewPersisted();
        Response response = getIndexClient().indexBooksRabbitMq();
        Message<Book> message = null;
        int count = 0;
        while ((message = (Message<Book>) amqpQueueChannel.receive(1000)) != null) {
            MessageHeaders headers = message.getHeaders();
            Long time = (Long) headers.get("timestamp");
            Book book = message.getPayload();
            book.hashCode();
            // if (time >= start && jobIds.contains(proxy.getId())) {
            // count++;
            // }
            count++;
        }
        Assert.assertTrue(count > 0);
    }

    @Test
    @Ignore("WIP")
    public void shouldIndexBooksKafka() throws Exception {
        bookData.getManyNewPersisted();
        Response response = getIndexClient().indexBooksKafka();
        Message<String> message = null;
        int count = 0;
        Thread.sleep(300000L);
        while ((message = (Message<String>) kafkaQueueChannel.receive(1000)) != null) {
            MessageHeaders headers = message.getHeaders();
            String book = message.getPayload();
            book.hashCode();
            // if (time >= start && jobIds.contains(proxy.getId())) {
            // count++;
            // }
            count++;
        }
        Assert.assertTrue(count > 0);
    }

    @Test
    public void shouldIndexBooksZero() throws Exception {
        bookData.getManyNewPersisted();
        Response response = getIndexClient().indexBooksZero();
        Message<String> message = null;
        int count = 0;
        while ((message = (Message<String>) zeroMqQueueChannel.receive(1000)) != null) {
            MessageHeaders headers = message.getHeaders();
            String book = message.getPayload();
            book.hashCode();
            // if (time >= start && jobIds.contains(proxy.getId())) {
            // count++;
            // }
            count++;
        }
        Assert.assertTrue(count > 0);
    }
    
    @Test
    public void performanceActive() throws Exception {
        long startTimeTx = System.currentTimeMillis();
        Fairy fairy = Fairy.create();
        Person person = fairy.person();
        String personStr = objectMapper.writeValueAsString(person);
        for (int i = 0; i < 1000000; i++) {
            toActiveDirectChannel.send(MessageBuilder.withPayload(personStr)
                    .build());
        }
        // capture times
        long successTimeTx = System.currentTimeMillis();
        long processTimeTx = successTimeTx - startTimeTx;

        // log details
        LOGGER.error(String.format("Activemq outbound processTime=%dms", processTimeTx));

        Message<String> message = null;
        int count = 0;
        startTimeTx = System.currentTimeMillis();
        while ((message = (Message<String>) jmsQueueChannel.receive(1000)) != null) {
            MessageHeaders headers = message.getHeaders();
            Long time = (Long) headers.get("timestamp");
            String string = message.getPayload();
//            Book book = objectMapper.readValue(string, Book.class);
//            book.hashCode();
            // if (time >= start && jobIds.contains(proxy.getId())) {
            // count++;
            // }
            count++;
        }
        Assert.assertTrue(count >= 1000000);
        // capture times
        successTimeTx = System.currentTimeMillis();
        processTimeTx = successTimeTx - startTimeTx;

        // log details
        LOGGER.error(String.format("Activemq inbound processTime=%dms", processTimeTx));
    }

    @Test
    public void performanceRabbit() throws Exception {
        long startTimeTx = System.currentTimeMillis();
        Fairy fairy = Fairy.create();
        Person person = fairy.person();
        String personStr = objectMapper.writeValueAsString(person);
        for (int i = 0; i < 1000000; i++) {
            toRabbitDirectChannel.send(MessageBuilder.withPayload(personStr)
                    .build());
        }
        // capture times
        long successTimeTx = System.currentTimeMillis();
        long processTimeTx = successTimeTx - startTimeTx;

        // log details
        LOGGER.error(String.format("Rabbitmq processTime=%dms", processTimeTx));
        
        Message<Person> message = null;
        int count = 0;
        startTimeTx = System.currentTimeMillis();
        while ((message = (Message<Person>) amqpQueueChannel.receive(1000)) != null) {
            MessageHeaders headers = message.getHeaders();
            Long time = (Long) headers.get("timestamp");
            Person string = message.getPayload();
//            Book book = objectMapper.readValue(string, Book.class);
//            book.hashCode();
            // if (time >= start && jobIds.contains(proxy.getId())) {
            // count++;
            // }
            count++;
        }
        Assert.assertTrue(count >= 1000000);
        // capture times
        successTimeTx = System.currentTimeMillis();
        processTimeTx = successTimeTx - startTimeTx;

        // log details
        LOGGER.error(String.format("Activemq inbound processTime=%dms", processTimeTx));
    }
    
    @Test
    public void performanceZero() throws Exception {
        long startTimeTx = System.currentTimeMillis();
        Fairy fairy = Fairy.create();
        Person person = fairy.person();
        String personStr = objectMapper.writeValueAsString(person);
        for (int i = 0; i < 1000000; i++) {
            toZeroDirectChannel.send(MessageBuilder.withPayload(personStr)
                    .build());
        }
        // capture times
        long successTimeTx = System.currentTimeMillis();
        long processTimeTx = successTimeTx - startTimeTx;

        // log details
        LOGGER.error(String.format("Zero outbound processTime=%dms", processTimeTx));
        
        Message<String> message = null;
        int count = 0;
        startTimeTx = System.currentTimeMillis();
        while ((message = (Message<String>) zeroMqQueueChannel.receive(1000)) != null) {
            MessageHeaders headers = message.getHeaders();
            Long time = (Long) headers.get("timestamp");
            String string = message.getPayload();
//            Book book = objectMapper.readValue(string, Book.class);
//            book.hashCode();
            // if (time >= start && jobIds.contains(proxy.getId())) {
            // count++;
            // }
            count++;
        }
        Assert.assertTrue(count >= 1000000);
        // capture times
        successTimeTx = System.currentTimeMillis();
        processTimeTx = successTimeTx - startTimeTx;

        // log details
        LOGGER.error(String.format("Zero inbound processTime=%dms", processTimeTx));
    }

    private UserClient getClient() {
        JSONProvider jsonP = new JSONProvider();
        jsonP.setSupportUnwrapped(true);
        jsonP.setDropRootElement(true);
        return JAXRSClientFactory.create(server.baseUri().toString()
                + "extensions-spring", UserClient.class,
                Arrays.asList(new JSONProvider[] { jsonP }));
    }

    private SearchIndexClient getIndexClient() {
        JSONProvider jsonP = new JSONProvider();
        jsonP.setSupportUnwrapped(true);
        jsonP.setDropRootElement(true);
        return JAXRSClientFactory.create(server.baseUri().toString()
                + "extensions-spring", SearchIndexClient.class,
                Arrays.asList(new JSONProvider[] { jsonP }));
    }

}
