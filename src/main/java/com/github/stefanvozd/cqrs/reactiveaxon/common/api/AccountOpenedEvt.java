package com.github.stefanvozd.cqrs.reactiveaxon.common.api;

import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.With;
import lombok.experimental.NonFinal;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@NonFinal
@Value
public class AccountOpenedEvt extends BankAccountEvt implements Serializable {

    UUID accountId;
    AccountHolder accountHolder;
    @With
    BigDecimal newBalance;

}
