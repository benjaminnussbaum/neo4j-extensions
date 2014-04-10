package org.neo4j.extensions.spring.zero.parser;

import org.springframework.integration.config.xml.AbstractIntegrationNamespaceHandler;

/**
 * The namespace handler for the ZeroMq namespace
 *
 * @author joemoore
 * @since 0.1
 *
 */
public class ZeroMqNamespaceHandler extends AbstractIntegrationNamespaceHandler {
	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.xml.NamespaceHandler#init()
	 */
	@Override
	public void init() {
		registerBeanDefinitionParser("inbound-channel-adapter",  new ZeroMqInboundChannelAdapterParser());
		registerBeanDefinitionParser("outbound-channel-adapter",  new ZeroMqOutboundChannelAdapterParser());
		registerBeanDefinitionParser("producer-context",  new ZeroMqProducerContextParser());
		registerBeanDefinitionParser("consumer-context",  new ZeroMqConsumerContextParser());
	}
}
