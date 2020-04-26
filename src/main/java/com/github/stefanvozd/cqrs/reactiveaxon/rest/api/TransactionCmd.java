package com.github.stefanvozd.cqrs.reactiveaxon.rest.api;

import java.math.BigDecimal;

public interface TransactionCmd extends BankAccountCmd {

    BigDecimal getAmount();
    String getDescription();

}
