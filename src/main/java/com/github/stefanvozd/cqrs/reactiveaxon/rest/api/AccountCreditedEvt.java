package com.github.stefanvozd.cqrs.reactiveaxon.rest.api;

import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.experimental.NonFinal;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@NonFinal
@Value
public class AccountCreditedEvt extends BankAccountEvt implements Serializable {

    UUID accountId;
    BigDecimal amount;
    String description;
    BigDecimal newBalance;

}
