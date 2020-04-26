package com.github.stefanvozd.cqrs.reactiveaxon.rest.projection.reactive;

import lombok.Value;

import java.util.UUID;

@Value
public class FindAccountProjectionProducedByCommand {

    UUID commandId;

}
