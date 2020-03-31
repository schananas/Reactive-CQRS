package com.github.stefanvozd.cqrs.reactiveaxon.api;

import lombok.Value;

import java.io.Serializable;
import java.util.UUID;

@Value
public class AccountClosedEvt implements Serializable {

    UUID accountId;
    AccountCloseReason reason;

}
