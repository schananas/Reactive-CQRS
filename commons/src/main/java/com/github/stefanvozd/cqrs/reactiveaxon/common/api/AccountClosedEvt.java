package com.github.stefanvozd.cqrs.reactiveaxon.common.api;

import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.experimental.NonFinal;

import java.io.Serializable;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@NonFinal
@Value
public class AccountClosedEvt extends BankAccountEvt implements Serializable {

    UUID accountId;
    AccountCloseReason reason;

}
