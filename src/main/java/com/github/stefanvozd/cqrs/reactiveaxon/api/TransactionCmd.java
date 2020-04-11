package com.github.stefanvozd.cqrs.reactiveaxon.api;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

public interface TransactionCmd extends BankAccountCmd {

    BigDecimal getAmount();
    String getDescription();

}
