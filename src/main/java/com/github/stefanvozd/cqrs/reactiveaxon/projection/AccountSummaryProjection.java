package com.github.stefanvozd.cqrs.reactiveaxon.projection;

import com.github.stefanvozd.cqrs.reactiveaxon.api.AccountClosedEvt;
import com.github.stefanvozd.cqrs.reactiveaxon.api.AccountCreditedEvt;
import com.github.stefanvozd.cqrs.reactiveaxon.api.AccountDebitedEvt;
import com.github.stefanvozd.cqrs.reactiveaxon.api.AccountOpenedEvt;
import com.github.stefanvozd.cqrs.reactiveaxon.api.TransactionEvt;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Slf4j
@Component
public class AccountSummaryProjection {

    private final AccountRepository accountRepository;

    public AccountSummaryProjection(
            AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }


    @Autowired
    public void accountOpenedEvtInputStream(Flux<AccountOpenedEvt> eventStream) {
        accountRepository.saveAll(eventStream
                .doOnNext(it->log.info(" + "))
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
                    .doOnNext(it->log.info(" ± "))
                    .flatMap(it-> accountRepository.updateBalance(it.getAccountId(),it.getNewBalance()),1)
               .subscribe();
    }


    @Autowired
    public void accountClosedEvtInputStream(Flux<AccountClosedEvt> eventStream) {
        eventStream
                 .doOnNext(it->log.info(" - "))
                 .flatMap(it->accountRepository.deleteByAccountId(it.getAccountId()),1)
                   .subscribe();
    }

    @Autowired
    public void accountCreditedEvtInputStream(Flux<AccountCreditedEvt> eventStream) {
        eventStream
                .doOnNext(it->log.info(" ± "))
                .flatMap(it-> accountRepository.updateBalance(it.getAccountId(),it.getNewBalance()),1)
                .subscribe();
    }


    @Autowired
    public void accountDebitedEvtInputStream(Flux<AccountDebitedEvt> eventStream) {
        eventStream
                .doOnNext(it->log.info(" ± "))
                .flatMap(it-> accountRepository.updateBalance(it.getAccountId(),it.getNewBalance()),1)
                .subscribe();
    }

}
