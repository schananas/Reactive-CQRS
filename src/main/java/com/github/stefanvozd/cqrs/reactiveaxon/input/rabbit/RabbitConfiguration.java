package com.github.stefanvozd.cqrs.reactiveaxon.input.rabbit;

import com.rabbitmq.client.Delivery;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;
import reactor.rabbitmq.ConsumeOptions;
import reactor.rabbitmq.ExceptionHandlers;
import reactor.rabbitmq.QueueSpecification;
import reactor.rabbitmq.RabbitFlux;
import reactor.rabbitmq.Receiver;
import reactor.rabbitmq.Sender;

import java.time.Duration;
import javax.annotation.PreDestroy;

@Configuration
public class RabbitConfiguration {

    @Bean
    Sender sender() {
        return RabbitFlux.createSender();
    }

    @Bean
    Receiver receiver() {
        return RabbitFlux.createReceiver();
    }

    @Bean
    Flux<Delivery> deliveryFlux(Sender sender,Receiver receiver) {
        sender.declareQueue(QueueSpecification.queue(Messaging.BANK_QUEUE))
              .subscribe();

        return receiver.consumeNoAck(Messaging.BANK_QUEUE, new ConsumeOptions()
                .exceptionHandler(new ExceptionHandlers.RetryAcknowledgmentExceptionHandler(
                        Duration.ofSeconds(10), Duration.ofMillis(500),
                        ExceptionHandlers.CONNECTION_RECOVERY_PREDICATE
                )));
    }

}
