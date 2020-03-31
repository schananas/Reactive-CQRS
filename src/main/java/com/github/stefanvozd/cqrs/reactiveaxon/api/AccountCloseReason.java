package com.github.stefanvozd.cqrs.reactiveaxon.api;

import java.io.Serializable;

public enum AccountCloseReason implements Serializable {

    CLOSED_BY_BANK,
    CLOSED_BY_CUSTOMER,
    OTHER,

}
