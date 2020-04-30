package com.github.stefanvozd.cqrs.reactiveaxon.common.api;

import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.experimental.NonFinal;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@NonFinal
@Value
public class AccountDebitedEvt extends BankAccountEvt implements TransactionEvt, Serializable {

    UUID accountId;
    BigDecimal amount;
    String description;
    BigDecimal newBalance;
    transient UUID producedByCommandId;

}
