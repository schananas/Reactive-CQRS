package com.github.stefanvozd.cqrs.reactiveaxon.rest.command;


import com.github.stefanvozd.cqrs.reactiveaxon.api.*;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.messaging.MetaData;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;
import static org.axonframework.modelling.command.AggregateLifecycle.markDeleted;

@Component
@Aggregate
@NoArgsConstructor
@Slf4j
public class BankAccount implements Serializable {

    private static final int SCALE = 2;

    @AggregateIdentifier
    private UUID accountId;

    private BigDecimal balance;

    @CommandHandler
    public BankAccount(OpenAccountCmd cmd) {
        log.debug("Handling {}", cmd);
        val newBalance = BigDecimal.ZERO.setScale(SCALE, RoundingMode.CEILING);
        apply(new AccountOpenedEvt(cmd.getAccountId(), cmd.getAccountHolder(), newBalance), MetaData.with("producedByCommandId",cmd.getCommandId()));
    }

    @CommandHandler
    public void handle(CreditAccountCmd cmd) {
        log.debug("Handling {}", cmd);
        validateTransactionCmd(cmd);
        val newBalance = balance.add(cmd.getAmount());
        apply(new AccountCreditedEvt(accountId, cmd.getAmount(), cmd.getDescription(), newBalance), MetaData.with("producedByCommandId",cmd.getCommandId()));
    }

    @CommandHandler
    public void handle(DebitAccountCmd cmd) {
        log.debug("Handling {}", cmd);
        validateTransactionCmd(cmd);
        val newBalance = balance.subtract(cmd.getAmount());
        apply(new AccountDebitedEvt(accountId, cmd.getAmount(), cmd.getDescription(), newBalance, cmd.getCommandId()), MetaData.with("producedByCommandId",cmd.getCommandId()));
    }

    private void validateTransactionCmd(TransactionCmd cmd) {
        if(cmd.getAmount().compareTo(BigDecimal.ZERO) <= 0) throw new IllegalArgumentException("Amount must be > 0");
        if(cmd.getAmount().scale() != SCALE) throw new IllegalArgumentException("Scale must be " + SCALE);
    }

    @CommandHandler
    public void handle(CloseAccountCmd cmd) {
        log.debug("Handling {}", cmd);
        apply(new AccountClosedEvt(accountId, cmd.getReason()), MetaData.with("producedByCommandId",cmd.getCommandId()));
    }

    @EventSourcingHandler
    public void on(AccountOpenedEvt evt) {
        log.debug("Applying {}", evt);
        accountId = evt.getAccountId();
        balance = evt.getNewBalance();
    }

    @EventSourcingHandler
    public void on(AccountCreditedEvt evt) {
        log.debug("Applying {}", evt);
        balance = evt.getNewBalance();
    }

    @EventSourcingHandler
    public void on(AccountDebitedEvt evt) {
        log.debug("Applying {}", evt);
        balance = evt.getNewBalance();
    }

    @EventSourcingHandler
    public void on(AccountClosedEvt evt) {
        log.debug("Applying {}", evt);
        markDeleted();
    }

}
