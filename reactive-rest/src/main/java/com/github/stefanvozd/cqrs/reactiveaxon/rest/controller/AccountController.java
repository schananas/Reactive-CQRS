package com.github.stefanvozd.cqrs.reactiveaxon.rest.controller;

import com.github.stefanvozd.cqrs.reactiveaxon.common.api.CloseAccountCmd;
import com.github.stefanvozd.cqrs.reactiveaxon.common.api.CreditAccountCmd;
import com.github.stefanvozd.cqrs.reactiveaxon.common.api.DebitAccountCmd;
import com.github.stefanvozd.cqrs.reactiveaxon.common.api.OpenAccountCmd;
import com.github.stefanvozd.cqrs.reactiveaxon.common.utils.ReactiveCommandGateway;
import com.github.stefanvozd.cqrs.reactiveaxon.rest.api.FindAccountUpdateByCommandId;
import com.github.stefanvozd.cqrs.reactiveaxon.rest.projection.AccountQueryUpdate;
import com.github.stefanvozd.cqrs.reactiveaxon.rest.projection.AccountSummary;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.queryhandling.SubscriptionQueryResult;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.function.Tuple2;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.UUID;
import java.util.function.Function;

@RestController
@RequestMapping("/account")
@Slf4j
public class AccountController {

    private final ReactiveCommandGateway reactiveCommandGateway;
    private final QueryGateway queryGateway;
    private final Retry retryStrategy;

    public AccountController(ReactiveCommandGateway reactiveCommandGateway,
                             QueryGateway queryGateway, Retry retryStrategy) {
        this.reactiveCommandGateway = reactiveCommandGateway;
        this.queryGateway = queryGateway;
        this.retryStrategy = retryStrategy;
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
                sendCommandMono
                        .retryWhen(retryStrategy)
                        .subscribeOn(Schedulers.parallel()),
                getAccountUpdateByCommandId(commandId)
                        .subscribeOn(Schedulers.parallel()))
                .map(Tuple2::getT2);
        //we only care about update result (T2)
        //T1 is command handler result
    }

    private Mono<AccountSummary> getAccountUpdateByCommandId(
            UUID commandId) {

        int TIMEOUT_SECONDS = 30;

        return callSubscriptionQuery(commandId)
                .retryWhen(retryStrategy)
                .map(it -> it.updates().doFinally(signal -> it.close()))
                .flatMapMany(Flux::from)
                .next() //wait for first update
                .map(AccountQueryUpdate::getAccountSummary)
                .timeout(Duration.ofSeconds(TIMEOUT_SECONDS));
    }

    //wrap SubscriptionQuery into lazy Mono, so we can use Reactor's retry mechanism
    //with retry mechanism our code will be resilient
    private Mono<SubscriptionQueryResult<Void, AccountQueryUpdate>> callSubscriptionQuery(UUID commandId) {
        return Mono.fromCallable(() ->
                queryGateway
                        .subscriptionQuery(
                                new FindAccountUpdateByCommandId(commandId),
                                ResponseTypes.instanceOf(Void.class), //we don't care about initial result set
                                ResponseTypes.instanceOf(AccountQueryUpdate.class))
        );
    }
}
