package com.github.stefanvozd.cqrs.reactiveaxon.projection.reactive;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Profile;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.util.UUID;

@Profile("reactive")
@Table("account_summary")
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccountSummary {

    @Id
    Long id;
    UUID accountId;
    String firstName;
    String lastName;
    BigDecimal balance;

}
