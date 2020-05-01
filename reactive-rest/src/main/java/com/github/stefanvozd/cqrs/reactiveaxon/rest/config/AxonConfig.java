package com.github.stefanvozd.cqrs.reactiveaxon.rest.config;

import org.axonframework.messaging.correlation.CorrelationDataProvider;
import org.axonframework.messaging.correlation.SimpleCorrelationDataProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.util.retry.Retry;

import java.time.Duration;

@Configuration
public class AxonConfig {

    @Bean
    public Retry retryStrategy() {
        return Retry.fixedDelay(3, Duration.ofSeconds(10));
    }

    //define correlation data provider for command id
    @Bean
    public CorrelationDataProvider producedByCommandIdProvider() {
        return new SimpleCorrelationDataProvider("producedByCommandId");
    }

}
