package com.github.stefanvozd.cqrs.reactiveaxon.rest.config;

import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.commandhandling.gateway.DefaultCommandGateway;
import org.axonframework.commandhandling.gateway.ExponentialBackOffIntervalRetryScheduler;
import org.axonframework.commandhandling.gateway.RetryScheduler;
import org.axonframework.messaging.correlation.CorrelationDataProvider;
import org.axonframework.messaging.correlation.SimpleCorrelationDataProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Configuration
public class AxonConfig {

    /* CommandGateway configured to use RetryScheduler */
    /* The RetryScheduler is capable of scheduling retries when command execution has failed. When a command fails due to an exception that is explicitly non-transient, no retries are done at all. Note that the retry scheduler is only invoked when a command fails due to a RuntimeException. Checked exceptions are regarded as a "business exception" and will never trigger a retry. */
    /* https://docs.axoniq.io/reference-guide/configuring-infrastructure-components/command-processing/command-dispatching#the-command-gateway */
    @Bean
    public CommandGateway commandGateway(CommandBus commandBus) {
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(5);
        RetryScheduler retryScheduler = ExponentialBackOffIntervalRetryScheduler.builder().retryExecutor(scheduledExecutorService).maxRetryCount(3).backoffFactor(1000).build();

        return DefaultCommandGateway.builder().commandBus(commandBus).retryScheduler(retryScheduler).build();
    }

    @Bean
    public CorrelationDataProvider producedByCommandIdProvider() {
        return new SimpleCorrelationDataProvider("producedByCommandId");
    }

}
