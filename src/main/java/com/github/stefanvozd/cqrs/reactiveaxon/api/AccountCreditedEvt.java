package com.github.stefanvozd.cqrs.reactiveaxon.api;

import lombok.Value;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;
import java.util.UUID;

@Value
public class AccountCreditedEvt implements TransactionEvt {

    UUID accountId;
    BigDecimal amount;
    String description;
    BigDecimal newBalance;

}
