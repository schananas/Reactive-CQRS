package com.github.stefanvozd.cqrs.reactiveaxon.rest.api;

import java.util.HashMap;
import java.util.Map;

public abstract class Event {
    public Map<String, Object> getMetaData() {
        if (metaData == null) {
            metaData = new HashMap<>();
        }
        return metaData;
    }

    protected transient Map<String, Object> metaData = new HashMap<>();

}
