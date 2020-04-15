package com.github.stefanvozd.cqrs.reactiveaxon.api;

import java.util.UUID;

public interface BankAccountCmd {

    UUID getAccountId();

    UUID commandId = UUID.randomUUID();

    default UUID getCommandId() {
        return commandId;
    }

}
