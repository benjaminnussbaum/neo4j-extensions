package org.neo4j.extensions.spring.rest;

import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.neo4j.extensions.client.SearchIndexClient;
import org.neo4j.extensions.spring.common.OutboundGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mediahound.graph.service.AlbumService;
import com.mediahound.graph.service.BookService;

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
    private AlbumService albumService;
    
    @Context
    private BookService bookService;
    
    @Context
    private OutboundGateway outboundGateway;
    
    private static final int BATCH_SIZE = 5;

    public Response indexBooksZero() {
        LOGGER.info(String.format("Book search index start"));
        long startTimeTx = System.currentTimeMillis();

        // capture times
        long successTimeTx = System.currentTimeMillis();
        long processTimeTx = successTimeTx - startTimeTx;

        // log details
        LOGGER.debug(String.format(
                "SearchIndexZeroController: TX: processTime=%dms",
                processTimeTx));
        Long booksSize = bookService.countAll();
        LOGGER.info(String.format("Total Books: %d", booksSize.intValue()));
        int totalPages = booksSize.intValue() % BATCH_SIZE;
        for (int i = 0; i <= totalPages; i++) {
            outboundGateway.sendBookRangeToActiveMq(i, BATCH_SIZE);
        }
        return Response.ok(String.format("Total Books: %d", booksSize.intValue())).build();
    }

    public Response indexBooksKafka() {
        LOGGER.info(String.format("Book search index start"));
        long startTimeTx = System.currentTimeMillis();

        // capture times
        long successTimeTx = System.currentTimeMillis();
        long processTimeTx = successTimeTx - startTimeTx;

        // log details
        LOGGER.debug(String.format(
                "SearchIndexKafkaController: TX: processTime=%dms",
                processTimeTx));
        Long booksSize = bookService.countAll();
        LOGGER.info(String.format("Total Books: %d", booksSize.intValue()));
        int totalPages = booksSize.intValue() % BATCH_SIZE;
        for (int i = 0; i <= totalPages; i++) {
            outboundGateway.sendBookRangeToKafka(i, BATCH_SIZE);
        }
        return Response.ok(String.format("Total Books: %d", booksSize.intValue())).build();
    }

    public Response indexBooksActiveMq() {
        LOGGER.info(String.format("Book search index start"));
        long startTimeTx = System.currentTimeMillis();

        // capture times
        long successTimeTx = System.currentTimeMillis();
        long processTimeTx = successTimeTx - startTimeTx;

        // log details
        LOGGER.debug(String.format(
                "SearchIndexActiveController: TX: processTime=%dms",
                processTimeTx));
        Long booksSize = bookService.countAll();
        LOGGER.info(String.format("Total Books: %d", booksSize.intValue()));
        int totalPages = booksSize.intValue() % BATCH_SIZE;
        for (int i = 0; i <= totalPages; i++) {
            outboundGateway.sendBookRangeToActiveMq(i, BATCH_SIZE);
        }
        return Response.ok(String.format("Total Books: %d", booksSize.intValue())).build();
    }

    public Response indexBooksRabbitMq() {
        LOGGER.info(String.format("Book search index start"));
        long startTimeTx = System.currentTimeMillis();

        // capture times
        long successTimeTx = System.currentTimeMillis();
        long processTimeTx = successTimeTx - startTimeTx;

        // log details
        LOGGER.debug(String.format(
                "SearchIndexRabbitController: TX: processTime=%dms",
                processTimeTx));
        Long booksSize = bookService.countAll();
        LOGGER.info(String.format("Total Books: %d", booksSize.intValue()));
        int totalPages = booksSize.intValue() % BATCH_SIZE;
        for (int i = 0; i <= totalPages; i++) {
            outboundGateway.sendBookRangeToRabbit(i, BATCH_SIZE);
        }
        return Response.ok(String.format("Total Books: %d", booksSize.intValue())).build();
    }
    
    public Response indexAlbumsZero() {
        LOGGER.info(String.format("Album search index start"));
        long startTimeTx = System.currentTimeMillis();

        // capture times
        long successTimeTx = System.currentTimeMillis();
        long processTimeTx = successTimeTx - startTimeTx;

        // log details
        LOGGER.debug(String.format(
                "SearchIndexZeroController: TX: processTime=%dms",
                processTimeTx));
        Long albumSize = albumService.countAll();
        LOGGER.info(String.format("Total Albums: %d", albumSize.intValue()));
        int totalPages = albumSize.intValue() % BATCH_SIZE;
        for (int i = 0; i <= totalPages; i++) {
            outboundGateway.sendAlbumRangeToActiveMq(i, BATCH_SIZE);
        }
        return Response.ok(String.format("Total Albums: %d", albumSize.intValue())).build();
    }

    public Response indexAlbumsKafka() {
        LOGGER.info(String.format("Album search index start"));
        long startTimeTx = System.currentTimeMillis();

        // capture times
        long successTimeTx = System.currentTimeMillis();
        long processTimeTx = successTimeTx - startTimeTx;

        // log details
        LOGGER.debug(String.format(
                "SearchIndexKafkaController: TX: processTime=%dms",
                processTimeTx));
        Long albumSize = albumService.countAll();
        LOGGER.info(String.format("Total Albums: %d", albumSize.intValue()));
        int totalPages = albumSize.intValue() % BATCH_SIZE;
        for (int i = 0; i <= totalPages; i++) {
            outboundGateway.sendAlbumRangeToKafka(i, BATCH_SIZE);
        }
        return Response.ok(String.format("Total Albums: %d", albumSize.intValue())).build();
    }

    public Response indexAlbumsActiveMq() {
        LOGGER.info(String.format("Album search index start"));
        long startTimeTx = System.currentTimeMillis();

        // capture times
        long successTimeTx = System.currentTimeMillis();
        long processTimeTx = successTimeTx - startTimeTx;

        // log details
        LOGGER.debug(String.format(
                "SearchIndexActiveController: TX: processTime=%dms",
                processTimeTx));
        Long albumSize = albumService.countAll();
        LOGGER.info(String.format("Total Albums: %d", albumSize.intValue()));
        int totalPages = albumSize.intValue() % BATCH_SIZE;
        for (int i = 0; i <= totalPages; i++) {
            outboundGateway.sendAlbumRangeToActiveMq(i, BATCH_SIZE);
        }
        return Response.ok(String.format("Total Albums: %d", albumSize.intValue())).build();
    }

    public Response indexAlbumsRabbitMq() {
        LOGGER.info(String.format("Album search index start"));
        long startTimeTx = System.currentTimeMillis();

        // capture times
        long successTimeTx = System.currentTimeMillis();
        long processTimeTx = successTimeTx - startTimeTx;

        // log details
        LOGGER.debug(String.format(
                "SearchIndexRabbitController: TX: processTime=%dms",
                processTimeTx));
        Long albumSize = albumService.countAll();
        LOGGER.info(String.format("Total Albums: %d", albumSize.intValue()));
        int totalPages = albumSize.intValue() % BATCH_SIZE;
        for (int i = 0; i <= totalPages; i++) {
            outboundGateway.sendAlbumRangeToRabbit(i, BATCH_SIZE);
        }
        return Response.ok(String.format("Total Albums: %d", albumSize.intValue())).build();
    }

}
