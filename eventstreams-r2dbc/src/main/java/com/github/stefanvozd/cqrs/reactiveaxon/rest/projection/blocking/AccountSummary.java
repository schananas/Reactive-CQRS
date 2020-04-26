package com.github.stefanvozd.cqrs.reactiveaxon.rest.projection.blocking;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Profile;


import java.math.BigDecimal;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

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

