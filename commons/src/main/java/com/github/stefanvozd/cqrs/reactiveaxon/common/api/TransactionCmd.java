package com.github.stefanvozd.cqrs.reactiveaxon.common.api;

import java.math.BigDecimal;

public interface TransactionCmd extends BankAccountCmd {

    BigDecimal getAmount();
    String getDescription();

}
