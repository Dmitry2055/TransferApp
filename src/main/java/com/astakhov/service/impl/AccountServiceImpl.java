package com.astakhov.service.impl;

import com.astakhov.dao.AccountDao;
import com.astakhov.entity.Account;
import com.astakhov.exception.ServiceException;
import com.astakhov.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class AccountServiceImpl implements AccountService {

    public static final Logger LOG = LoggerFactory.getLogger(AccountServiceImpl.class);

    public AccountServiceImpl(final AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    private final AccountDao accountDao;

    @Override
    public void updateAccount(final Account account) {
        LOG.info("Updating account {}", account.getId());
        accountDao.update(account);
    }

    @Override
    public Account createAccount(final BigDecimal balance) {
        LOG.info("Creating a new account");
        if (BigDecimal.ZERO.compareTo(balance) > 0) {
            throw new ServiceException("Cannot create an account with negative balance");
        }
        Account account = new Account();
        account.setBalance(balance);
        account.setId(UUID.randomUUID().toString());
        accountDao.create(account);
        LOG.info("Created a new account {}", account.getId());
        return account;
    }

    @Override
    public Optional<Account> getAccount(final String id) {
        return Optional.ofNullable(accountDao.getAccountById(id));
    }

    @Override
    public List<Account> getAccounts() {
        return accountDao.getAccounts();
    }
}
