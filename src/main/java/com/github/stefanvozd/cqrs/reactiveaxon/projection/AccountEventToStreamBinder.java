package com.github.stefanvozd.cqrs.reactiveaxon.projection;


import com.github.stefanvozd.cqrs.reactiveaxon.api.AccountClosedEvt;
import com.github.stefanvozd.cqrs.reactiveaxon.api.AccountCreditedEvt;
import com.github.stefanvozd.cqrs.reactiveaxon.api.AccountDebitedEvt;
import com.github.stefanvozd.cqrs.reactiveaxon.api.AccountOpenedEvt;
import com.github.stefanvozd.cqrs.reactiveaxon.api.TransactionEvt;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.eventhandling.ResetHandler;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import reactor.core.publisher.FluxSink;

@ProcessingGroup("evtHandlers")
@Component
@Slf4j
public class AccountEventToStreamBinder {

    private final AccountRepository accountRepository;

    public AccountEventToStreamBinder(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @EventHandler
    public void on(AccountOpenedEvt evt,
                   @Qualifier("accountOpenedEvtOutputStream") FluxSink<AccountOpenedEvt> accountOpenedEvtOutputStream) {
        accountOpenedEvtOutputStream.next(evt);
    }

    @EventHandler
    public void on(TransactionEvt evt,
                   @Qualifier("transactionEvtOutputStream") FluxSink<TransactionEvt> eventStream) {
        eventStream.next(evt);
    }

    @EventHandler
    public void on(AccountClosedEvt evt,
                   @Qualifier("accountClosedEvtOutputStream") FluxSink<AccountClosedEvt> eventStream) {
        eventStream.next(evt);
    }

    @EventHandler
    public void on(AccountCreditedEvt evt,
                   @Qualifier("accountCreditedEvtOutputStream") FluxSink<AccountCreditedEvt> accountCreditedEvtOutputStream) {
        accountCreditedEvtOutputStream.next(evt);
    }

    @EventHandler
    public void on(AccountDebitedEvt evt, @Qualifier("accountDebitedEvtOutputStream") FluxSink<AccountDebitedEvt> accountDebitedEvtOutputStream) {
        accountDebitedEvtOutputStream.next(evt);
    }

    @ResetHandler
    public void onReset() {
        log.info("Handling ResetTriggeredEvent on Account");
        accountRepository.deleteAll().subscribe();
    }
}
