package com.github.stefanvozd.cqrs.reactiveaxon;

import com.github.stefanvozd.cqrs.reactiveaxon.command.BankAccount;
import org.axonframework.common.caching.Cache;
import org.axonframework.common.lock.PessimisticLockFactory;
import org.axonframework.eventsourcing.AggregateFactory;
import org.axonframework.eventsourcing.CachingEventSourcingRepository;
import org.axonframework.eventsourcing.NoSnapshotTriggerDefinition;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.axonframework.messaging.annotation.ParameterResolverFactory;
import org.axonframework.modelling.command.Repository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;


@EnableR2dbcRepositories("com.github.stefanvozd.cqrs.reactiveaxon.projection")
@SpringBootApplication
public class ReactiveCqrsApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReactiveCqrsApplication.class, args);
	}

}
