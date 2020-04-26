package com.github.stefanvozd.cqrs.reactiveaxon.rest.projection.reactive;


import com.github.stefanvozd.cqrs.reactiveaxon.rest.api.AccountClosedEvt;
import com.github.stefanvozd.cqrs.reactiveaxon.rest.api.AccountCreditedEvt;
import com.github.stefanvozd.cqrs.reactiveaxon.rest.api.AccountDebitedEvt;
import com.github.stefanvozd.cqrs.reactiveaxon.rest.api.AccountOpenedEvt;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.eventhandling.ResetHandler;
import org.axonframework.messaging.annotation.MetaDataValue;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import reactor.core.publisher.FluxSink;

import java.util.UUID;

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
    public void on(AccountOpenedEvt evt, @MetaDataValue("producedByCommandId") UUID producedByCommandId,
                   @Qualifier("accountOpenedEvtOutputStream") FluxSink<AccountOpenedEvt> accountOpenedEvtOutputStream) {
        evt.getMetaData().put("producedByCommandId",producedByCommandId);
        accountOpenedEvtOutputStream.next(evt);
    }

    @EventHandler
    public void on(AccountClosedEvt evt, @MetaDataValue("producedByCommandId") UUID producedByCommandId,
                   @Qualifier("accountClosedEvtOutputStream") FluxSink<AccountClosedEvt> eventStream) {
        evt.getMetaData().put("producedByCommandId",producedByCommandId);
        eventStream.next(evt);
    }

    @EventHandler
    public void on(AccountCreditedEvt evt, @MetaDataValue("producedByCommandId") UUID producedByCommandId,
                   @Qualifier("accountCreditedEvtOutputStream") FluxSink<AccountCreditedEvt> eventStream) {
        evt.getMetaData().put("producedByCommandId",producedByCommandId);
        eventStream.next(evt);
    }

    @EventHandler
    public void on(AccountDebitedEvt evt, @MetaDataValue("producedByCommandId") UUID producedByCommandId,
                   @Qualifier("accountDebitedEvtOutputStream") FluxSink<AccountDebitedEvt> eventStream) {
        evt.getMetaData().put("producedByCommandId",producedByCommandId);
        eventStream.next(evt);
    }

    @ResetHandler
    public void onReset() {
        log.info("Handling ResetTriggeredEvent on Account");
        r2dbcAccountRepository.deleteAll().subscribe();
    }

    @QueryHandler
    public Boolean handle(FindAccountProjectionProducedByCommand query) {
        return true;
    }

}
