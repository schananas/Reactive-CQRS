package com.github.stefanvozd.cqrs.reactiveaxon.common.api;

import java.io.Serializable;
import java.util.UUID;

public interface BankAccountCmd extends Serializable {

    UUID getAccountId();

    UUID commandId = UUID.randomUUID();

    default UUID getCommandId() {
        return commandId;
    }

}
