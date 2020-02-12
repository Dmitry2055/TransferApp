package com.astakhov.dao;

import com.astakhov.entity.Account;

import java.util.List;

public interface AccountDao {
    Account getAccountById(String accountId);

    void create(Account toAccount);

    List<Account> getAccounts();

    void update(Account account);
}
