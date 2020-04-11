package com.github.stefanvozd.cqrs.reactiveaxon.projection.reactive;

import com.github.stefanvozd.cqrs.reactiveaxon.api.AccountClosedEvt;
import com.github.stefanvozd.cqrs.reactiveaxon.api.AccountCreditedEvt;
import com.github.stefanvozd.cqrs.reactiveaxon.api.AccountDebitedEvt;
import com.github.stefanvozd.cqrs.reactiveaxon.api.AccountOpenedEvt;
import com.github.stefanvozd.cqrs.reactiveaxon.api.TransactionEvt;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Profile("reactive")
@Slf4j
@Component
public class AccountSummaryProjection {

    private final R2dbcAccountRepository r2dbcAccountRepository;

    public AccountSummaryProjection(
            R2dbcAccountRepository r2dbcAccountRepository) {
        this.r2dbcAccountRepository = r2dbcAccountRepository;
    }


    @Autowired
    public void accountOpenedEvtInputStream(Flux<AccountOpenedEvt> eventStream) {
        r2dbcAccountRepository.saveAll(eventStream
                                          .doOnNext(it->log.debug(" + "))
                                          .map(evt-> new AccountSummary(
                                                  null,
                                                  evt.getAccountId(),
                                                  evt.getAccountHolder().getFirstName(),
                                                  evt.getAccountHolder().getLastName(),
                                                  evt.getNewBalance())))
                              .subscribe();
    }


    @Autowired
    public void transactionEvtInputStream(Flux<TransactionEvt> eventStream) {
            eventStream
                    .doOnNext(it->log.debug(" ± "))
                    .concatMap(it-> r2dbcAccountRepository.updateBalance(it.getAccountId(), it.getNewBalance()))
               .subscribe();
    }


    @Autowired
    public void accountClosedEvtInputStream(Flux<AccountClosedEvt> eventStream) {
        eventStream
                 .doOnNext(it->log.debug(" - "))
                 .concatMap(it-> r2dbcAccountRepository.deleteByAccountId(it.getAccountId()))
                 .subscribe();
    }

    @Autowired
    public void accountCreditedEvtInputStream(Flux<AccountCreditedEvt> eventStream) {
        eventStream
                .doOnNext(it->log.debug(" ± "))
                .concatMap(it-> r2dbcAccountRepository.updateBalance(it.getAccountId(), it.getNewBalance()))
                .subscribe();
    }


    @Autowired
    public void accountDebitedEvtInputStream(Flux<AccountDebitedEvt> eventStream) {
        eventStream
                .doOnNext(it->log.debug(" ± "))
                .concatMap(it-> r2dbcAccountRepository.updateBalance(it.getAccountId(), it.getNewBalance()))
                .subscribe();
    }

}
