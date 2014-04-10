package org.neo4j.extensions.spring.zero;

import org.springframework.integration.Message;
import org.springframework.integration.handler.AbstractMessageHandler;

/**
 * @author joemoore
 * @since 0.1
 */
public class ZeroMqProducerMessageHandler extends AbstractMessageHandler {

	private final ZeroMqProducerContext zeroMqProducerContext;

	public ZeroMqProducerMessageHandler(final ZeroMqProducerContext zeroMqProducerContext) {
		this.zeroMqProducerContext = zeroMqProducerContext;
	}

	public ZeroMqProducerContext getZeroMqProducerContext() {
		return zeroMqProducerContext;
	}

	@Override
	protected void handleMessageInternal(final Message<?> message) throws Exception {
	    zeroMqProducerContext.send(message);
	}
}
