package com.github.stefanvozd.cqrs.reactiveaxon.projection.reactive;

import com.github.stefanvozd.cqrs.reactiveaxon.api.AccountClosedEvt;
import com.github.stefanvozd.cqrs.reactiveaxon.api.AccountCreditedEvt;
import com.github.stefanvozd.cqrs.reactiveaxon.api.AccountDebitedEvt;
import com.github.stefanvozd.cqrs.reactiveaxon.api.AccountOpenedEvt;
import com.github.stefanvozd.cqrs.reactiveaxon.api.TransactionEvt;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

@Profile("reactive")
@Configuration
public class ProjectionStreamEmitter {

    private final int BUFFER_SIZE = 2500;

    //AccountOpenedEvt
    EmitterProcessor<AccountOpenedEvt> accountOpenedEvtInputStream = EmitterProcessor.create(BUFFER_SIZE);
    FluxSink<AccountOpenedEvt> accountOpenedEvtOutputStream = accountOpenedEvtInputStream.sink();

    @Bean
    public Flux<AccountOpenedEvt> accountOpenedEvtInputStream() {
        return accountOpenedEvtInputStream.publish(BUFFER_SIZE).autoConnect();
    }

    @Bean
    public FluxSink<AccountOpenedEvt> accountOpenedEvtOutputStream() {
        return accountOpenedEvtOutputStream;
    }

    //TransactionEvt
    EmitterProcessor<TransactionEvt> transactionEvtInputStream = EmitterProcessor.create(BUFFER_SIZE);
    FluxSink<TransactionEvt> transactionEvtOutputStream = transactionEvtInputStream.sink();

    @Bean
    public Flux<TransactionEvt> transactionEvtInputStream() {
        return transactionEvtInputStream.publish(BUFFER_SIZE).autoConnect();
    }

    @Bean
    public FluxSink<TransactionEvt> transactionEvtOutputStream() {
        return transactionEvtOutputStream;
    }



    //AccountClosedEvt
    EmitterProcessor<AccountClosedEvt> accountClosedEvtInputStream = EmitterProcessor.create(BUFFER_SIZE);
    FluxSink<AccountClosedEvt> accountClosedEvtOutputStream = accountClosedEvtInputStream.sink();

    @Bean
    public Flux<AccountClosedEvt> accountClosedEvtInputStream() {
        return accountClosedEvtInputStream.publish(BUFFER_SIZE).autoConnect();

    }

    @Bean
    public FluxSink<AccountClosedEvt> accountClosedEvtOutputStream() {
        return accountClosedEvtOutputStream;
    }


    //AccountCreditedEvt
    EmitterProcessor<AccountCreditedEvt> accountCreditedEvtInputStream = EmitterProcessor.create(BUFFER_SIZE);
    FluxSink<AccountCreditedEvt> accountCreditedEvtOutputStream = accountCreditedEvtInputStream.sink();

    @Bean
    public Flux<AccountCreditedEvt> accountCreditedEvtInputStream() {
        return accountCreditedEvtInputStream.publish(BUFFER_SIZE).autoConnect();
    }

    @Bean
    public FluxSink<AccountCreditedEvt> accountCreditedEvtOutputStream() {
        return accountCreditedEvtOutputStream;
    }


    //AccountDebitedEvt
    EmitterProcessor<AccountDebitedEvt> accountDebitedEvtInputStream = EmitterProcessor.create(BUFFER_SIZE);
    FluxSink<AccountDebitedEvt> accountDebitedEvtOutputStream = accountDebitedEvtInputStream.sink();

    @Bean
    public Flux<AccountDebitedEvt> accountDebitedEvtInputStream() {
        return accountDebitedEvtInputStream.publish(BUFFER_SIZE).autoConnect();
    }

    @Bean
    public FluxSink<AccountDebitedEvt> accountDebitedEvtOutputStream() {
        return accountDebitedEvtOutputStream;
    }

}
