package com.github.stefanvozd.cqrs.reactiveaxon.projection;

import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.UUID;

@Repository public
interface AccountRepository extends ReactiveCrudRepository<AccountSummary, String> {

    @Modifying
    @Query("UPDATE account_summary SET balance = :balance WHERE account_id = :accountId")
    Mono<Boolean> updateBalance(UUID accountId, BigDecimal balance);

    @Modifying
    @Query("DELETE from account_summary WHERE account_id=:accountId")
    Mono<Boolean> deleteByAccountId(UUID accountId);

}