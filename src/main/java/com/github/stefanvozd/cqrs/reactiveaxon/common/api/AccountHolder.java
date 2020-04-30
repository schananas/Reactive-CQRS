package com.github.stefanvozd.cqrs.reactiveaxon.common.api;

import lombok.Value;

import java.io.Serializable;
import java.time.LocalDate;

@Value
public class AccountHolder implements Serializable {

    String firstName;
    String lastName;
    LocalDate dateOfBirth;

}
