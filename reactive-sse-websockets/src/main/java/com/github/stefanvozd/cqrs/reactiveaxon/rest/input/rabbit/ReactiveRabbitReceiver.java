package com.github.stefanvozd.cqrs.reactiveaxon.rest.input.rabbit;

import com.github.stefanvozd.cqrs.reactiveaxon.api.BankAccountCmd;
import com.github.stefanvozd.cqrs.reactiveaxon.rest.utils.ReactiveCommandGateway;
import com.rabbitmq.client.Delivery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.util.SerializationUtils;
import reactor.core.publisher.Flux;

import javax.annotation.PostConstruct;
import java.time.Duration;

@Profile("reactive")
@Component
@Slf4j
public class ReactiveRabbitReceiver {

    private final Flux<Delivery> deliveryFlux;
    private final ReactiveCommandGateway reactiveCommandGateway;

    public ReactiveRabbitReceiver(
            Flux<Delivery> deliveryFlux,
            ReactiveCommandGateway reactiveCommandGateway) {
        this.deliveryFlux = deliveryFlux;
        this.reactiveCommandGateway = reactiveCommandGateway;
    }

    private BankAccountCmd toCommand(Delivery msg) {
        return (BankAccountCmd) SerializationUtils.deserialize(msg.getBody());
    }

    @PostConstruct
    public void startReceivingSequentialBackPressure(){
        Flux<BankAccountCmd> inputStream = deliveryFlux
                .map(this::toCommand)
                .doOnRequest(l -> log.debug("Requesting next {} commands", l))
                .doOnNext(it -> log.debug("Command delivered: {}", it));

        reactiveCommandGateway.sendAll(inputStream)
                .delaySubscription(Duration.ofSeconds(5))
                .subscribe();
    }

}
