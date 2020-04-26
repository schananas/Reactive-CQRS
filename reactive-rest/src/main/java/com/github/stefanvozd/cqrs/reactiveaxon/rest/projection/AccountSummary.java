package com.github.stefanvozd.cqrs.reactiveaxon.rest.projection;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "account_summary")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountSummary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    UUID accountId;
    String firstName;
    String lastName;
    BigDecimal balance;

}

