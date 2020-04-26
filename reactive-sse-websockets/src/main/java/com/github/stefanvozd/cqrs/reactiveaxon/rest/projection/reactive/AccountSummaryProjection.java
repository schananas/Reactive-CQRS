package com.github.stefanvozd.cqrs.reactiveaxon.rest.projection.reactive;

import com.github.stefanvozd.cqrs.reactiveaxon.api.*;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.queryhandling.QueryUpdateEmitter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Profile("reactive")
@Slf4j
@Component
public class AccountSummaryProjection {

    private final R2dbcAccountRepository r2dbcAccountRepository;

    private final QueryUpdateEmitter queryUpdateEmitter;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public AccountSummaryProjection(
            R2dbcAccountRepository r2dbcAccountRepository,
            QueryUpdateEmitter queryUpdateEmitter) {
        this.r2dbcAccountRepository = r2dbcAccountRepository;
        this.queryUpdateEmitter = queryUpdateEmitter;
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
                                it.getNewBalance()))
                        .zipWith(Mono.just((UUID) it.getMetaData().get("producedByCommandId")))
                )
                .doOnNext(it -> emitAccountUpdate(it.getT1(), it.getT2(), QueryEventType.ADDED))
                .subscribe();
    }

    @Autowired
    public void accountClosedEvtInputStream(Flux<AccountClosedEvt> eventStream) {
        eventStream
                .doOnNext(it -> log.debug("Projection: Handling AccountClosedEvt"))
                .concatMap(it -> r2dbcAccountRepository
                        .deleteByAccountId(it.getAccountId())
                        .zipWith(Mono.just((UUID) it.getMetaData().get("producedByCommandId")))
                )
                .doOnNext(it -> emitAccountUpdate(it.getT1(), it.getT2(), QueryEventType.REMOVED))
                .subscribe();
    }

    @Autowired
    public void accountCreditedEvtInputStream(Flux<AccountCreditedEvt> eventStream) {
        eventStream
                .doOnNext(it -> log.debug("Projection: Handling AccountCreditedEvt"))
                .concatMap(it -> r2dbcAccountRepository
                        .updateBalance(it.getAccountId(), it.getNewBalance())
                        .zipWith(Mono.just((UUID) it.getMetaData().get("producedByCommandId")))
                )
                .doOnNext(it -> emitAccountUpdate(it.getT1(), it.getT2(), QueryEventType.UPDATED))
                .subscribe();
    }


    @Autowired
    public void accountDebitedEvtInputStream(Flux<AccountDebitedEvt> eventStream) {
        eventStream
                .doOnNext(it -> log.debug("Projection: Handling AccountDebitedEvt"))
                .concatMap(it -> r2dbcAccountRepository
                        .updateBalance(it.getAccountId(), it.getNewBalance())
                        .zipWith(Mono.just((UUID) it.getMetaData().get("producedByCommandId")))
                )
                .doOnNext(it -> emitAccountUpdate(it.getT1(), it.getT2(), QueryEventType.UPDATED))
                .subscribe();
    }

    private void emitAccountUpdate(AccountSummary accountSummary, UUID producedByCommandId, QueryEventType type) {
        queryUpdateEmitter.emit(
                FindAccountProjectionProducedByCommand.class,
                query -> query.getCommandId().equals(producedByCommandId),
                new AccountQueryUpdate(accountSummary, producedByCommandId, type)
        );
    }
}
