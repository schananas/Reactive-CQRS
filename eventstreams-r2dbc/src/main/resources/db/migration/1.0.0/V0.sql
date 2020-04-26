
CREATE TABLE IF NOT EXISTS account_summary
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


CREATE TABLE IF NOT EXISTS token_entry
(
    processor_name VARCHAR(255) NOT NULL,
    segment        INTEGER      NOT NULL,
    owner          VARCHAR(255),
    timestamp      VARCHAR(255) NOT NULL,
    token          oid,
    token_type     VARCHAR(255),
    PRIMARY KEY (processor_name, segment)
);