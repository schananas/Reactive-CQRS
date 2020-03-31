package com.github.stefanvozd.cqrs.reactiveaxon.api;

import lombok.Value;
import lombok.experimental.Wither;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

@Value
public class AccountOpenedEvt implements Serializable {

    UUID accountId;
    AccountHolder accountHolder;
    @Wither BigDecimal newBalance;

}
