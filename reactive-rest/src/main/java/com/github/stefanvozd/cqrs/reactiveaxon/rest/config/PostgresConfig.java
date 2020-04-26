package com.github.stefanvozd.cqrs.reactiveaxon.rest.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories("com.github.stefanvozd.cqrs.reactiveaxon.rest")
public class PostgresConfig {

}
