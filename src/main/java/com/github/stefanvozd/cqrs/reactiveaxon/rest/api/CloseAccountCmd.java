package com.github.stefanvozd.cqrs.reactiveaxon.rest.api;

import lombok.Value;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.io.Serializable;
import java.util.UUID;

@Value
public class CloseAccountCmd implements BankAccountCmd, Serializable {

    @TargetAggregateIdentifier UUID accountId;
    AccountCloseReason reason;

}
