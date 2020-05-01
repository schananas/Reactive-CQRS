package com.github.stefanvozd.cqrs.reactiveaxon.rest.projection;


import com.github.stefanvozd.cqrs.reactiveaxon.common.api.*;
import com.github.stefanvozd.cqrs.reactiveaxon.rest.api.FindAccountUpdateByCommandId;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.eventhandling.ResetHandler;
import org.axonframework.messaging.annotation.MetaDataValue;
import org.axonframework.queryhandling.QueryHandler;
import org.axonframework.queryhandling.QueryUpdateEmitter;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
public class AccountSummaryProjection {

    private final AccountRepository accountRepository;
    private final QueryUpdateEmitter queryUpdateEmitter;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public AccountSummaryProjection(AccountRepository accountRepository, QueryUpdateEmitter queryUpdateEmitter) {
        this.accountRepository = accountRepository;
        this.queryUpdateEmitter = queryUpdateEmitter;
    }

    @EventHandler
    public void on(AccountOpenedEvt evt, @MetaDataValue("producedByCommandId") UUID producedByCommandId,
                   AccountRepository accountRepository) {
        //correlation data provider will provide us with command id from event meta-data
        AccountSummary accountSummary = accountRepository.save(new AccountSummary(
                null,
                evt.getAccountId(),
                evt.getAccountHolder().getFirstName(),
                evt.getAccountHolder().getLastName(),
                evt.getNewBalance()));
        //attach command id to an update
        emitAccountUpdate(accountSummary, producedByCommandId, QueryEventType.ADDED);
    }

    @EventHandler
    public void on(AccountClosedEvt evt, @MetaDataValue("producedByCommandId") UUID producedByCommandId, AccountRepository accountRepository) {
        AccountSummary accountSummary = accountRepository.deleteByAccountId(evt.getAccountId());
        emitAccountUpdate(accountSummary, producedByCommandId, QueryEventType.REMOVED);
    }

    @EventHandler
    public void on(AccountCreditedEvt evt, @MetaDataValue("producedByCommandId") UUID producedByCommandId, AccountRepository accountRepository) {
        AccountSummary accountSummary = accountRepository.updateBalance(evt.getAccountId(), evt.getNewBalance());
        emitAccountUpdate(accountSummary, producedByCommandId, QueryEventType.UPDATED);
    }

    @EventHandler
    public void on(AccountDebitedEvt evt, @MetaDataValue("producedByCommandId") UUID producedByCommandId, AccountRepository accountRepository) {
        AccountSummary accountSummary = accountRepository.updateBalance(evt.getAccountId(), evt.getNewBalance());
        emitAccountUpdate(accountSummary, producedByCommandId, QueryEventType.UPDATED);
    }

    @QueryHandler
    public Boolean handle(FindAccountUpdateByCommandId query) {
        // used for fetching initial result, not needed in this demo
        // by will cause AXONIQ-5000 (NO_HANDLER_FOR_QUERY) error if skipped
        return true;
    }


    private void emitAccountUpdate(AccountSummary accountSummary, UUID producedByCommandId, QueryEventType type) {
        queryUpdateEmitter.emit(
                FindAccountUpdateByCommandId.class,
                //this query defines that u can only get updates that are produced by provided command id
                query -> query.getCommandId().equals(producedByCommandId),
                new AccountQueryUpdate(accountSummary, producedByCommandId, type)
        );
    }

    @ResetHandler
    public void onReset() {
        log.info("Handling ResetTriggeredEvent on Account");
        accountRepository.deleteAll();
    }

}
