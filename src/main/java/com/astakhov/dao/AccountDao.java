package com.astakhov.dao;

import com.astakhov.entity.Account;

import java.util.List;

/**
 * DAO for {@link Account} entities.
 */
public interface AccountDao {

    /**
     * Get {@link Account} by its id.
     * @param accountId account id
     * @return {@link Account} that matches provided id
     */
    Account getAccountById(String accountId);

    /**
     * Persists provided {@link Account} object.
     * @param account an {@link Account} object to create.
     */
    void create(Account account);

    /**
     * Retrieves all accounts.
     * @return a {@link List} of all persisted {@link Account} objects.
     */
    List<Account> getAccounts();

    /**
     * Updates an existing account. If provided account does not exist - it will not be created.
     * @param account an updated {@link Account} object.
     */
    void update(Account account);
}
