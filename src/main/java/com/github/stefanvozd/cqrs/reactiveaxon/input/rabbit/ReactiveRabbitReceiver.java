package com.github.stefanvozd.cqrs.reactiveaxon.input.rabbit;

import com.github.stefanvozd.cqrs.reactiveaxon.api.BankAccountCmd;
import com.github.stefanvozd.cqrs.reactiveaxon.utils.ReactiveCommandGateway;
import com.github.stefanvozd.cqrs.reactiveaxon.utils.ReactiveEventBus;
import com.rabbitmq.client.Delivery;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventhandling.EventMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import org.springframework.util.SerializationUtils;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.UUID;
import javax.annotation.PostConstruct;

@DependsOn({"bankAccount"})
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
                .doOnNext(it->log.debug("Command delivered: {}",it))
                .doOnRequest(l->log.debug("Requesting next {} commands",l));

        reactiveCommandGateway.<BankAccountCmd,UUID>sendAll(inputStream)
                .delaySubscription(Duration.ofSeconds(5))
                .subscribe();
    }

}
