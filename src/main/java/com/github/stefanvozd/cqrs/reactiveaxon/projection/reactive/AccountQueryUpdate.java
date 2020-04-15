package com.github.stefanvozd.cqrs.reactiveaxon.projection.reactive;

import com.github.stefanvozd.cqrs.reactiveaxon.api.QueryEventType;
import lombok.Value;

import java.util.UUID;

@Value
public class AccountQueryUpdate {

    AccountSummary accountSummary;
    UUID producedByCommandId;
    QueryEventType type;

}
