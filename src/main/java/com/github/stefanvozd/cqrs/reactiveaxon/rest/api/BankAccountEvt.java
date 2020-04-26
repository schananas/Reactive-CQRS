package com.github.stefanvozd.cqrs.reactiveaxon.rest.api;

import java.util.UUID;

public abstract class BankAccountEvt extends Event {

    abstract UUID getAccountId();

    UUID producedByCommandId = null;

}
