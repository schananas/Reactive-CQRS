package com.github.stefanvozd.cqrs.reactiveaxon.api;

import java.util.UUID;

public interface BankAccountEvt {

    UUID getAccountId();

    UUID producedByCommandId = null;

}
