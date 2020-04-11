package com.github.stefanvozd.cqrs.reactiveaxon.api;

import lombok.Value;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.io.Serializable;
import java.util.UUID;

@Value
public class OpenAccountCmd implements BankAccountCmd, Serializable {

    @TargetAggregateIdentifier UUID accountId;
    AccountHolder accountHolder;

}
