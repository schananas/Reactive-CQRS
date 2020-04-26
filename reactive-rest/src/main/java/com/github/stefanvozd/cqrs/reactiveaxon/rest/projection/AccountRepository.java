package com.github.stefanvozd.cqrs.reactiveaxon.rest.projection;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.UUID;

@Repository
public interface AccountRepository extends CrudRepository<AccountSummary, String> {

    @Modifying
    @Query(value = "UPDATE account_summary SET balance = :balance WHERE account_id = :accountId", nativeQuery = true)
    AccountSummary updateBalance(UUID accountId, BigDecimal balance);

    @Modifying
    @Query(value = "DELETE from account_summary WHERE account_id=:accountId", nativeQuery = true)
    AccountSummary deleteByAccountId(UUID accountId);

}