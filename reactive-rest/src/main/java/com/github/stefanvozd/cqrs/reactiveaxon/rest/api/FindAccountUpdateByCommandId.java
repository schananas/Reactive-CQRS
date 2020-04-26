package com.github.stefanvozd.cqrs.reactiveaxon.rest.api;

import lombok.Value;

import java.util.UUID;

@Value
public class FindAccountUpdateByCommandId {
    UUID commandId;
}
