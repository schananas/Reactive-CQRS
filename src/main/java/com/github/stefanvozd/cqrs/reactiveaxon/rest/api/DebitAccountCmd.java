package com.github.stefanvozd.cqrs.reactiveaxon.rest.api;

import lombok.Value;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

@Value
public class DebitAccountCmd implements TransactionCmd ,Serializable {

    @TargetAggregateIdentifier UUID accountId;
    BigDecimal amount;
    String description;

}
