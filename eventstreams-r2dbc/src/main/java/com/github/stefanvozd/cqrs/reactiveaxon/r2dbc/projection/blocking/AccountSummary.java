package com.github.stefanvozd.cqrs.reactiveaxon.r2dbc.projection.blocking;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Profile;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Profile("blocking")
@Table(name= "account_summary")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountSummary {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    Long id;
    UUID accountId;
    String firstName;
    String lastName;
    BigDecimal balance;

}

