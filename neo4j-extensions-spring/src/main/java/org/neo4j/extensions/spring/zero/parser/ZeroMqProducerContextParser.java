package org.neo4j.extensions.spring.zero.parser;

import org.neo4j.extensions.spring.zero.ZeroMqProducerContext;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSimpleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

/**
 * @author joemoore
 * @since 0.1
 */
public class ZeroMqProducerContextParser extends
        AbstractSimpleBeanDefinitionParser {

    @Override
    protected Class<?> getBeanClass(final Element element) {
        return ZeroMqProducerContext.class;
    }

    @Override
    protected void doParse(final Element element,
            final ParserContext parserContext,
            final BeanDefinitionBuilder builder) {
        final String zeroMQPort = element.getAttribute("port");

        Integer number = null;
        try {
            number = Integer.parseInt(zeroMQPort);
        } catch (Exception e) {}
        if (number != null) {
            builder.addConstructorArgValue(zeroMQPort);
        }
    }

}
