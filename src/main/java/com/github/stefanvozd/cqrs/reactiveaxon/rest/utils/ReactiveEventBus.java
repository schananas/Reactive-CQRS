package com.github.stefanvozd.cqrs.reactiveaxon.rest.utils;

import org.axonframework.eventhandling.EventMessage;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.function.BiFunction;

@Profile("reactive")
@Configuration
@Component
public class ReactiveEventBus {

    private final EventStore eventStore;

    private EmitterProcessor<EventMessage> eventStream = EmitterProcessor.create(1000);
    private FluxSink<EventMessage> eventsInputStream = eventStream.sink();

    public ReactiveEventBus(EventStore eventStore) {
        this.eventStore = eventStore;
    }

    @PostConstruct
    public void init() {
        eventStore.registerDispatchInterceptor(this::handle);
    }

    public BiFunction<Integer, EventMessage<?>, EventMessage<?>> handle(List<? extends EventMessage<?>> messages) {
        messages.forEach(event -> eventsInputStream.next(event));
        return (i, m) -> m;
    }

    @Bean
    public Flux<EventMessage> eventStream() {
        return eventStream.publish().autoConnect();
    }

}
