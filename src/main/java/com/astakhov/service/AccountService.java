package com.astakhov.service;

import com.astakhov.entity.Account;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Service for working with accounts.
 */
public interface AccountService {

    /**
     * Updates an existing account. If account does not exist - no account will be created.
     * @param account account to update
     */
    void updateAccount(Account account);

    /**
     * Creates and saves a new account.
     * @param balance balance on new account
     * @return created {@link Account object.}
     */
    Account createAccount(BigDecimal balance);

    /**
     * Gets account by id. If account does not exist - {@link Optional} will be empty.
     * @param id id of account to get
     * @return {@link Optional} of {@link Account}
     */
    Optional<Account> getAccount(String id);

    /**
     * Gets list of all accounts.
     * @return list of all accounts
     */
    List<Account> getAccounts();
}
