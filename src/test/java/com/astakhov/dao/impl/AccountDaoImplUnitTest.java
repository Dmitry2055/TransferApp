package com.astakhov.dao.impl;

import com.astakhov.dao.AccountDao;
import com.astakhov.entity.Account;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class AccountDaoImplUnitTest {

    private Map<String, Account> storage = new HashMap<>();
    private AccountDao accountDao = new AccountImplDao(storage);

    @Test
    public void shouldReturnAccountWhenExistsTest() {
        Account dummyAccount = new Account();
        dummyAccount.setBalance(BigDecimal.valueOf(123.45));
        dummyAccount.setId("existingAccountId");
        storage.put(dummyAccount.getId(), dummyAccount);
        accountDao = new AccountImplDao(storage);
        assertNotNull(accountDao.getAccountById("existingAccountId"));
    }

    @Test
    public void shouldReturnNullWhenAccountNotExist() {
        assertNull(accountDao.getAccountById("nonexistentAccountId"));
    }

    @Test
    public void shouldPersistAccountWhenSaveInvoked() {
        Account dummyAccount = new Account();
        dummyAccount.setBalance(BigDecimal.valueOf(123.45));
        dummyAccount.setId("existingAccountId");
        accountDao.create(dummyAccount);
        assertEquals(dummyAccount, storage.get(dummyAccount.getId()));
    }
}