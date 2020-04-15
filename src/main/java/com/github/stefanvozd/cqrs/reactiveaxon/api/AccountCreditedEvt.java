package com.github.stefanvozd.cqrs.reactiveaxon.api;

import lombok.AllArgsConstructor;
import lombok.Value;
import lombok.experimental.NonFinal;

import java.math.BigDecimal;
import java.util.UUID;

@AllArgsConstructor
@NonFinal
@Value
public class AccountCreditedEvt implements BankAccountEvt, TransactionEvt {

    UUID accountId;
    BigDecimal amount;
    String description;
    BigDecimal newBalance;
    UUID producedByCommandId;

}
