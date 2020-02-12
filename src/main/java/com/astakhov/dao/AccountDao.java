package com.astakhov.dao;

import com.astakhov.entity.Account;

import java.util.List;

public interface AccountDao {
    Account getAccountById(String accountId);

    void save(Account toAccount);

    List<Account> getAccounts();
}
