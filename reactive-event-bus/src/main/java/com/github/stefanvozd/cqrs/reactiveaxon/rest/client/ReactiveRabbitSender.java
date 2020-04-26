package com.github.stefanvozd.cqrs.reactiveaxon.rest.client;

import com.github.stefanvozd.cqrs.reactiveaxon.rest.generator.BankAccountCmdGenerator;
import com.github.stefanvozd.cqrs.reactiveaxon.rest.input.rabbit.Messaging;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.SerializationUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.FluxSink;
import reactor.rabbitmq.OutboundMessage;
import reactor.rabbitmq.Sender;

import java.util.Objects;

@RestController
public class ReactiveRabbitSender {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReactiveRabbitSender.class);

    private final Sender sender;

    public ReactiveRabbitSender(Sender sender) {
        this.sender = sender;
    }


    @GetMapping("/send/rabbit")
    public void rabbitSender(@RequestParam("commands") int commands) {
        BankAccountCmdGenerator bankAccountCmdGenerator = new BankAccountCmdGenerator(50000);

        EmitterProcessor<Object> data = EmitterProcessor.create(commands + 1);
        FluxSink<Object> sink = data.sink();

        for (int i = 0; i < commands; i++) {
            sink.next(bankAccountCmdGenerator.next());
        }

        sender.send(
                data
                        .doOnNext(it -> LOGGER.debug("SENDING"))
                        .map(it -> new OutboundMessage("", Messaging.BANK_QUEUE, Objects.requireNonNull(SerializationUtils.serialize(it)))))
                .subscribe();
    }
}
