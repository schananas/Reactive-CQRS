package com.github.stefanvozd.cqrs.reactiveaxon.r2dbc.projection.reactive;


import com.github.stefanvozd.cqrs.reactiveaxon.common.api.AccountClosedEvt;
import com.github.stefanvozd.cqrs.reactiveaxon.common.api.AccountCreditedEvt;
import com.github.stefanvozd.cqrs.reactiveaxon.common.api.AccountDebitedEvt;
import com.github.stefanvozd.cqrs.reactiveaxon.common.api.AccountOpenedEvt;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.eventhandling.ResetHandler;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import reactor.core.publisher.FluxSink;

@Profile("reactive")
@ProcessingGroup("reactiveEventHandlers")
@Component
@Slf4j
public class AccountEventToStreamBinder {

    private final R2dbcAccountRepository r2dbcAccountRepository;

    public AccountEventToStreamBinder(R2dbcAccountRepository r2dbcAccountRepository) {
        this.r2dbcAccountRepository = r2dbcAccountRepository;
    }

    @EventHandler
    public void on(AccountOpenedEvt evt, @Qualifier("accountOpenedEvtOutputStream") FluxSink<AccountOpenedEvt> accountOpenedEvtOutputStream) {
        accountOpenedEvtOutputStream.next(evt);
    }

    @EventHandler
    public void on(AccountClosedEvt evt, @Qualifier("accountClosedEvtOutputStream") FluxSink<AccountClosedEvt> eventStream) {
        eventStream.next(evt);
    }

    @EventHandler
    public void on(AccountCreditedEvt evt, @Qualifier("accountCreditedEvtOutputStream") FluxSink<AccountCreditedEvt> eventStream) {
        eventStream.next(evt);
    }

    @EventHandler
    public void on(AccountDebitedEvt evt, @Qualifier("accountDebitedEvtOutputStream") FluxSink<AccountDebitedEvt> eventStream) {
        eventStream.next(evt);
    }

    @ResetHandler
    public void onReset() {
        log.info("Handling ResetTriggeredEvent on Account");
        r2dbcAccountRepository.deleteAll().subscribe();
    }

}
