package com.github.stefanvozd.cqrs.reactiveaxon.projection.reactive;

import lombok.Value;

import java.util.UUID;

@Value
public class FindProjectionProducedByCommand {

    UUID commandId;

}
