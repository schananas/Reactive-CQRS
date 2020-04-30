package com.github.stefanvozd.cqrs.reactiveaxon.r2dbc.projection.reactive;

import com.github.stefanvozd.cqrs.reactiveaxon.common.api.AccountClosedEvt;
import com.github.stefanvozd.cqrs.reactiveaxon.common.api.AccountCreditedEvt;
import com.github.stefanvozd.cqrs.reactiveaxon.common.api.AccountDebitedEvt;
import com.github.stefanvozd.cqrs.reactiveaxon.common.api.AccountOpenedEvt;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.queryhandling.QueryUpdateEmitter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Profile("reactive")
@Slf4j
@Component
public class AccountSummaryProjection {

    private final R2dbcAccountRepository r2dbcAccountRepository;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public AccountSummaryProjection(
            R2dbcAccountRepository r2dbcAccountRepository,
            QueryUpdateEmitter queryUpdateEmitter) {
        this.r2dbcAccountRepository = r2dbcAccountRepository;
    }


    @Autowired
    public void accountOpenedEvtInputStream(Flux<AccountOpenedEvt> eventStream) {
        eventStream
                .doOnNext(it -> log.debug("Projection: Handling AccountOpenedEvt"))
                .concatMap(it -> r2dbcAccountRepository
                        .save(new AccountSummary(
                                null,
                                it.getAccountId(),
                                it.getAccountHolder().getFirstName(),
                                it.getAccountHolder().getLastName(),
                                it.getNewBalance())))
                .subscribe();
    }

    @Autowired
    public void accountClosedEvtInputStream(Flux<AccountClosedEvt> eventStream) {
        eventStream
                .doOnNext(it -> log.debug("Projection: Handling AccountClosedEvt"))
                .concatMap(it -> r2dbcAccountRepository
                        .deleteByAccountId(it.getAccountId()))
                .subscribe();
    }

    @Autowired
    public void accountCreditedEvtInputStream(Flux<AccountCreditedEvt> eventStream) {
        eventStream
                .doOnNext(it -> log.debug("Projection: Handling AccountCreditedEvt"))
                .concatMap(it -> r2dbcAccountRepository
                        .updateBalance(it.getAccountId(), it.getNewBalance()))
                .subscribe();
    }


    @Autowired
    public void accountDebitedEvtInputStream(Flux<AccountDebitedEvt> eventStream) {
        eventStream
                .doOnNext(it -> log.debug("Projection: Handling AccountDebitedEvt"))
                .concatMap(it -> r2dbcAccountRepository
                        .updateBalance(it.getAccountId(), it.getNewBalance()))
                .subscribe();
    }


}
