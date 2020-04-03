package com.github.stefanvozd.cqrs.reactiveaxon.input.rabbit;

import com.github.stefanvozd.cqrs.reactiveaxon.utils.AxonBackpressureSubscriber;
import com.github.stefanvozd.cqrs.reactiveaxon.utils.ReactiveCommandGateway;
import com.rabbitmq.client.Delivery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import org.springframework.util.SerializationUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import javax.annotation.PostConstruct;

@DependsOn({"bankAccount"})
@Component
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
    public void startReceiving(){
        AxonBackpressureSubscriber<Object> aSubcriber = new AxonBackpressureSubscriber<>();

        deliveryFlux
                .delaySubscription(Duration.ofSeconds(5))
                .map(this::toCommand)
                .doOnNext(it->  LOGGER.info("COMMAND {}",it))
                .flatMap(this::sendCommand,1)
                .limitRate(1)
                .onBackpressureError()
                .subscribe(aSubcriber);
    }

    private Object toCommand(Delivery msg) {
        return SerializationUtils.deserialize(msg.getBody());
    }

    private Mono<Object> sendCommand(Object c) {
        return reactiveCommandGateway.send(c)
                                     .doOnSuccess(it-> LOGGER.info("SENT {}", it));
    }

    //todo Request/reply example
}
