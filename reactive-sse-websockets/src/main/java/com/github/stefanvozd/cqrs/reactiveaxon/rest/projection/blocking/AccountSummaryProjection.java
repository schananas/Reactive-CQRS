package com.github.stefanvozd.cqrs.reactiveaxon.rest.projection.blocking;


import com.github.stefanvozd.cqrs.reactiveaxon.api.AccountClosedEvt;
import com.github.stefanvozd.cqrs.reactiveaxon.api.AccountCreditedEvt;
import com.github.stefanvozd.cqrs.reactiveaxon.api.AccountDebitedEvt;
import com.github.stefanvozd.cqrs.reactiveaxon.api.AccountOpenedEvt;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.eventhandling.ResetHandler;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("blocking")
@ProcessingGroup("blockingEventHandlers")
@Component
@Slf4j
public class AccountSummaryProjection {

    private final AccountRepository accountRepository;

    public AccountSummaryProjection(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @EventHandler
    public void on(AccountOpenedEvt evt,
                   AccountRepository accountRepository) {
        accountRepository.save(new AccountSummary(
                null,
                evt.getAccountId(),
                evt.getAccountHolder().getFirstName(),
                evt.getAccountHolder().getLastName(),
                evt.getNewBalance()));
    }

    @EventHandler
    public void on(AccountClosedEvt evt, AccountRepository accountRepository) {
        accountRepository.deleteByAccountId(evt.getAccountId());
    }

    @EventHandler
    public void on(AccountCreditedEvt evt, AccountRepository accountRepository) {
        accountRepository.updateBalance(evt.getAccountId(), evt.getNewBalance());
    }

    @EventHandler
    public void on(AccountDebitedEvt evt, AccountRepository accountRepository) {
        accountRepository.updateBalance(evt.getAccountId(), evt.getNewBalance());
    }

    @ResetHandler
    public void onReset() {
        log.info("Handling ResetTriggeredEvent on Account");
        accountRepository.deleteAll();
    }
}
