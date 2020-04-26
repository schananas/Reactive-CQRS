package com.github.stefanvozd.cqrs.reactiveaxon.rest.controller;

import com.github.stefanvozd.cqrs.reactiveaxon.rest.api.*;
import com.github.stefanvozd.cqrs.reactiveaxon.rest.projection.AccountQueryUpdate;
import com.github.stefanvozd.cqrs.reactiveaxon.rest.projection.AccountSummary;
import com.github.stefanvozd.cqrs.reactiveaxon.rest.utils.ReactiveCommandGateway;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.queryhandling.SubscriptionQueryResult;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.function.Tuple2;

import java.time.Duration;
import java.util.UUID;
import java.util.function.Function;

@RestController
@RequestMapping("/account")
@Slf4j
public class AccountController {

    private final ReactiveCommandGateway reactiveCommandGateway;
    private final QueryGateway queryGateway;

    public AccountController(ReactiveCommandGateway reactiveCommandGateway,
                             QueryGateway queryGateway) {
        this.reactiveCommandGateway = reactiveCommandGateway;
        this.queryGateway = queryGateway;
    }

    @PostMapping
    public Mono<AccountSummary> openAccount(@RequestBody OpenAccountCmd command) {
        return reactiveCommandGateway
                .send(command)
                .transform(subscribeForAccountUpdate(command.getCommandId()));
    }

    @PostMapping("/credit")
    public Mono<AccountSummary> creditAccount(@RequestBody CreditAccountCmd command) {
        return reactiveCommandGateway
                .send(command)
                .transform(subscribeForAccountUpdate(command.getCommandId()));
    }

    @PostMapping("/debit")
    public Mono<AccountSummary> debitAccount(@RequestBody DebitAccountCmd command) {
        return reactiveCommandGateway
                .send(command)
                .transform(subscribeForAccountUpdate(command.getCommandId()));
    }

    @DeleteMapping
    public Mono<AccountSummary> deleteAccount(@RequestBody CloseAccountCmd command) {
        return reactiveCommandGateway
                .send(command)
                .transform(subscribeForAccountUpdate(command.getCommandId()));
    }

    private Function<Mono<Object>, Mono<AccountSummary>> subscribeForAccountUpdate(UUID commandId) {
        //use Mono.zip to run Mono's in parallel and wait until they are done
        //each Mono should use subscribeOn(Scheduler) to get out of the common thread from where they're merged
        //this is a safe way not to miss an update, which could happen if we first send command and then subscribe for updates
        return (sendCommandMono) -> Mono.zip(
                sendCommandMono.subscribeOn(Schedulers.parallel()),
                getAccountUpdateByCommandId(commandId)).subscribeOn(Schedulers.parallel())
                .map(Tuple2::getT2); //we only care about update result
    }

    private Mono<AccountSummary> getAccountUpdateByCommandId(
            UUID commandId) {
        SubscriptionQueryResult<Void, AccountQueryUpdate> queryResult = queryGateway
                .subscriptionQuery(
                        new FindAccountUpdateByCommandId(commandId),
                        ResponseTypes.instanceOf(Void.class), //we dont care about initial result set
                        ResponseTypes.instanceOf(AccountQueryUpdate.class));

        int TIMEOUT_SECONDS = 5;
        return queryResult
                .updates()
                .next()
                .map(AccountQueryUpdate::getAccountSummary)
                .timeout(Duration.ofSeconds(TIMEOUT_SECONDS))
                .doFinally(it -> queryResult.close());
    }
}