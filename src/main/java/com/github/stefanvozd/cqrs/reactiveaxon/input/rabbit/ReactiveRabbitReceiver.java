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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;

@DependsOn({"bankAccount"})
@Component
@Slf4j
public class ReactiveRabbitReceiver {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReactiveRabbitReceiver.class);

    private final Flux<Delivery> deliveryFlux;
    private final ReactiveCommandGateway reactiveCommandGateway;

    public ReactiveRabbitReceiver(
            Flux<Delivery> deliveryFlux,
            ReactiveCommandGateway reactiveCommandGateway) {
        this.deliveryFlux = deliveryFlux;
        this.reactiveCommandGateway = reactiveCommandGateway;
    }

    @PostConstruct
    public void startReceivingParallelBackPressure(){
        deliveryFlux
                .map(this::toCommand)
                .groupBy(BankAccountCmd::getAccountId)
                .flatMap(this::sendWithBackPressure,3)
        .subscribe();
    }

    private BankAccountCmd toCommand(Delivery msg) {
        return (BankAccountCmd) SerializationUtils.deserialize(msg.getBody());
    }

    public Flux<Object> sendWithBackPressure(Flux<BankAccountCmd> commands)  {
        return commands
                .doOnNext(it -> LOGGER.info("COMMAND {}", it))
                .concatMap(this::sendCommand);
    }

    private Mono<Object> sendCommand(Object c) {
        return reactiveCommandGateway.send(c)
                                     .doOnSuccess(it-> LOGGER.info("SENT {}", it));
    }

//    @PostConstruct
//    public void startReceivingSequentialBackPressure(){
//        Flux<BankAccountCmd> inputStream = deliveryFlux.map(this::toCommand);
//
//        sendWithBackPressure(inputStream)
//                .delaySubscription(Duration.ofSeconds(5))
//                .subscribe();
//    }




    //todo Request/reply example
}
