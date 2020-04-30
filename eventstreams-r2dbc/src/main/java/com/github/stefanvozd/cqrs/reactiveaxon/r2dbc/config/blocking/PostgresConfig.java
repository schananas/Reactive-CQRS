package com.github.stefanvozd.cqrs.reactiveaxon.r2dbc.config.blocking;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Profile("blocking")
@Configuration
@EnableJpaRepositories("com.github.stefanvozd.cqrs.reactiveaxon.r2dbc.projection.blocking")
public class PostgresConfig {


}
