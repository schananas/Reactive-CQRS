package com.github.stefanvozd.cqrs.reactiveaxon.projection;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.util.UUID;

@Table("account_summary")
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccountSummary {

    @Id
    private Long id;
    UUID accountId;
    String firstName;
    String lastName;
    BigDecimal balance;

}
/*
create table account_summary
(
	account_id uuid not null,
	first_name varchar,
	last_name varchar,
	balance real,
	id bigserial not null
		constraint account_summary_pk
			primary key
);

alter table account_summary owner to postgres;

create unique index account_summary_id_uindex
	on account_summary (id);



*/

