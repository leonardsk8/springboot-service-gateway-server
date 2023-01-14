package com.apringboot.app.gateway.filters.factory;

import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
public class FactoryGatewayFilterFactory extends AbstractGatewayFilterFactory<FactoryGatewayFilterFactory.ConfigClass> {

    private final Logger logger = LoggerFactory.getLogger(FactoryGatewayFilterFactory.class);

    public FactoryGatewayFilterFactory() {
        super(ConfigClass.class);
    }

    @Override
    public GatewayFilter apply(ConfigClass config) {

        logger.info("Executing pre gateway filter factory " + config.message);
        return (exchange, chain) -> chain.filter(exchange).then(Mono.fromRunnable(() -> {

            Optional.ofNullable(config.cookieValue).ifPresent(cookie -> {
                exchange.getResponse().addCookie(ResponseCookie.from(config.cookieName, cookie).build());
            });

            logger.info("Executing post gateway filter factory" + config.message);

        }));
    }

    @Override
    public String name() {
        return "ExampleFactory";
    }

    @Override
    public List<String> shortcutFieldOrder() {
        return Arrays.asList("message","cookieName","cookieValue");
    }

    @Getter
    @Setter
    public static class ConfigClass {

        private String message;
        private String cookieValue;
        private String cookieName;
    }
}
