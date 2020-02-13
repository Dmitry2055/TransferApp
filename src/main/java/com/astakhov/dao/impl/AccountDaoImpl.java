package com.astakhov.dao.impl;

import com.astakhov.dao.AccountDao;
import com.astakhov.entity.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class AccountDaoImpl implements AccountDao {

    private static final Logger LOG = LoggerFactory.getLogger(AccountDaoImpl.class);

    private final Map<String, Account> storage;

    public AccountDaoImpl(final Map<String, Account> storage) {
        this.storage = storage;
    }

    @Override
    public Account getAccountById(final String accountId) {
        return storage.get(accountId);
    }

    @Override
    public void create(final Account account) {
        storage.put(account.getId(), account);
    }

    @Override
    public List<Account> getAccounts() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public void update(final Account account) {
        if (storage.containsKey(account.getId())) {
            storage.put(account.getId(), account);
        }
    }
}
