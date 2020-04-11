package com.github.stefanvozd.cqrs.reactiveaxon.client;

import com.github.stefanvozd.cqrs.reactiveaxon.generator.BankAccountCmdGenerator;
import com.github.stefanvozd.cqrs.reactiveaxon.input.rabbit.Messaging;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.SerializationUtils;
import org.springframework.web.bind.annotation.GetMapping;
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
    public void rabbitSender() {
        BankAccountCmdGenerator bankAccountCmdGenerator = new BankAccountCmdGenerator(50);

        EmitterProcessor<Object> data = EmitterProcessor.create(100000);
        FluxSink<Object> sink = data.sink();

        for (int i = 0; i<1000; i++) {
            sink.next(bankAccountCmdGenerator.next());
        }

       sender.send(
                data.onBackpressureError()
                    .doOnNext(it -> LOGGER.debug("SENDING"))
                    .map(it -> new OutboundMessage("", Messaging.BANK_QUEUE,Objects.requireNonNull(SerializationUtils.serialize(it)))))
                    .subscribe();
    }
}
