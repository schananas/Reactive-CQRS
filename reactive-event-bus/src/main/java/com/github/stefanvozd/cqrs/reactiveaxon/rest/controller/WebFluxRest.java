package com.github.stefanvozd.cqrs.reactiveaxon.rest.controller;

import com.github.stefanvozd.cqrs.reactiveaxon.api.*;
import com.github.stefanvozd.cqrs.reactiveaxon.rest.projection.reactive.AccountQueryUpdate;
import com.github.stefanvozd.cqrs.reactiveaxon.rest.projection.reactive.AccountSummary;
import com.github.stefanvozd.cqrs.reactiveaxon.rest.projection.reactive.FindAccountProjectionProducedByCommand;
import com.github.stefanvozd.cqrs.reactiveaxon.rest.api.*;
import com.github.stefanvozd.cqrs.reactiveaxon.rest.utils.ReactiveCommandGateway;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.queryhandling.SubscriptionQueryResult;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Profile("reactive")
@RestController
@RequestMapping("/account")
@Slf4j
public class WebFluxRest {

    private final ReactiveCommandGateway reactiveCommandGateway;
    private final QueryGateway queryGateway;

    private final int TIMEOUT_SECONDS = 15;

    public WebFluxRest(ReactiveCommandGateway reactiveCommandGateway,
                       QueryGateway queryGateway) {
        this.reactiveCommandGateway = reactiveCommandGateway;
        this.queryGateway = queryGateway;
    }

    @PostMapping
    public Mono<AccountSummary> openAccount(@RequestBody OpenAccountCmd openAccountCmd) {
        return reactiveCommandGateway
                .send(openAccountCmd)
                .then(getProjectionProducedByCommand(openAccountCmd));
    }

    @PostMapping("/credit")
    public Mono<AccountSummary> creditAccount(@RequestBody CreditAccountCmd creditAccountCmd) {
        return reactiveCommandGateway
                .send(creditAccountCmd)
                .then(getProjectionProducedByCommand(creditAccountCmd));
    }

    @PostMapping("/debit")
    public Mono<AccountSummary> debitAccount(@RequestBody DebitAccountCmd debitAccountCmd) {
        return reactiveCommandGateway
                .send(debitAccountCmd)
                .then(getProjectionProducedByCommand(debitAccountCmd));
    }

    @DeleteMapping
    public Mono<AccountSummary> deleteAccount(@RequestBody CloseAccountCmd closeAccountCmd) {
        return reactiveCommandGateway
                .send(closeAccountCmd)
                .then(getProjectionProducedByCommand(closeAccountCmd));
    }

    private Mono<AccountSummary> getProjectionProducedByCommand(
            @RequestBody BankAccountCmd openAccountCmd) {
        SubscriptionQueryResult<Void, AccountQueryUpdate> queryResult = queryGateway
                .subscriptionQuery(
                        new FindAccountProjectionProducedByCommand(openAccountCmd.getCommandId()),
                        ResponseTypes.instanceOf(Void.class),
                        ResponseTypes.instanceOf(AccountQueryUpdate.class));

        return queryResult
                .updates()
                .next()
                .map(AccountQueryUpdate::getAccountSummary)
                .timeout(Duration.ofSeconds(TIMEOUT_SECONDS))
                .doFinally(it -> queryResult.close());
    }
}
