package com.github.stefanvozd.cqrs.reactiveaxon.r2dbc.projection.reactive;

import com.github.stefanvozd.cqrs.reactiveaxon.common.api.QueryEventType;
import lombok.Value;

@Value
public class AccountQueryUpdate {

    AccountSummary accountSummary;
    QueryEventType type;

}
