package com.astakhov.dao.impl;

import com.astakhov.dao.AccountDao;
import com.astakhov.entity.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class AccountImplDao implements AccountDao {

    private static final Logger LOG = LoggerFactory.getLogger(AccountImplDao.class);

    private final Map<String, Account> storage;

    public AccountImplDao(Map<String, Account> storage) {
        this.storage = storage;
    }

    @Override
    public Account getAccountById(final String accountId) {
        return storage.get(accountId);
    }

    @Override
    public void create(final Account toAccount) {
        storage.put(toAccount.getId(), toAccount);
    }

    @Override
    public List<Account> getAccounts() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public void update(Account account) {

    }
}
