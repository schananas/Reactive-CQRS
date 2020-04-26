package com.github.stefanvozd.cqrs.reactiveaxon.rest.projection;

import com.github.stefanvozd.cqrs.reactiveaxon.rest.api.QueryEventType;
import lombok.Value;

import java.util.UUID;

@Value
public class AccountQueryUpdate {

    AccountSummary accountSummary;
    UUID producedByCommandId;
    QueryEventType type;

}
