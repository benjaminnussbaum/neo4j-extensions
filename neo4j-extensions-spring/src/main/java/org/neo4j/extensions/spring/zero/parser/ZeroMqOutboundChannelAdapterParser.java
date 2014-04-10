package org.neo4j.extensions.spring.zero.parser;

import org.neo4j.extensions.spring.zero.ZeroMqProducerMessageHandler;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.integration.config.xml.AbstractOutboundChannelAdapterParser;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;

/**
 *
 * @author joemoore
 * @since 0.5
 *
 */
public class ZeroMqOutboundChannelAdapterParser extends AbstractOutboundChannelAdapterParser {
	@Override
	protected AbstractBeanDefinition parseConsumer(final Element element, final ParserContext parserContext) {
		final BeanDefinitionBuilder zeroMqProducerMessageHandlerBuilder =
								BeanDefinitionBuilder.genericBeanDefinition(ZeroMqProducerMessageHandler.class);

		final String zeroMQServerBeanName = element.getAttribute("zeromq-producer-context-ref");

		if (StringUtils.hasText(zeroMQServerBeanName)) {
			zeroMqProducerMessageHandlerBuilder.addConstructorArgReference(zeroMQServerBeanName);
		}

		return zeroMqProducerMessageHandlerBuilder.getBeanDefinition();
	}
}
