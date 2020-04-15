package com.github.stefanvozd.cqrs.reactiveaxon.api;

import lombok.Value;
import lombok.With;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

@Value
public class AccountOpenedEvt implements BankAccountEvt, Serializable {

    UUID accountId;
    AccountHolder accountHolder;
    @With
    BigDecimal newBalance;
    UUID producedByCommandId;

}
