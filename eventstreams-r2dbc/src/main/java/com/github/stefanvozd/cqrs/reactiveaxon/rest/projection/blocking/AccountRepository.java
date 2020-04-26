package com.github.stefanvozd.cqrs.reactiveaxon.rest.projection.blocking;

import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.UUID;

@Profile("blocking")
@Repository
public interface AccountRepository extends CrudRepository<AccountSummary, String> {

    @Modifying
    @Query(value="UPDATE account_summary SET balance = :balance WHERE account_id = :accountId", nativeQuery = true)
    void updateBalance(UUID accountId, BigDecimal balance);

    @Modifying
    @Query(value="DELETE from account_summary WHERE account_id=:accountId", nativeQuery = true)
    void deleteByAccountId(UUID accountId);

}