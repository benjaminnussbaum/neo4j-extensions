package org.neo4j.extensions.spring.common.listener;


import javax.jms.ExceptionListener;
import javax.jms.JMSException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Lazy;
import org.springframework.jms.connection.CachingConnectionFactory;

/**
 * Listens for exception that occur and logs them.
 *
 * @author bradnussbaum
 * @version 1.0.0
 * @since 1.0.0
 */
@Configurable
public class ConnectionFactoryExceptionListener implements ExceptionListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConnectionFactoryExceptionListener.class);

    @Autowired(required = false)
    @Lazy
    private CachingConnectionFactory cachingConnectionFactory;

    @Override
    public void onException(JMSException exception) {
        LOGGER.error(exception.getMessage(), exception);
        if (cachingConnectionFactory != null) {
            cachingConnectionFactory.onException(exception);
        }
    }
}
