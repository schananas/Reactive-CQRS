package com.github.stefanvozd.cqrs.reactiveaxon.rest.input.rabbit;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface Messaging {
    String BANK_QUEUE = "bank_queue";

    @Input(BANK_QUEUE)
    SubscribableChannel getBankQueueChannel();
}
