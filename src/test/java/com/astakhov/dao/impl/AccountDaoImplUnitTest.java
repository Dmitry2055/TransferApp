package com.astakhov.dao.impl;

import com.astakhov.entity.Account;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class AccountDaoImplUnitTest {

    private Map<String, Account> storage = new HashMap<>();
    private AccountDaoImpl accountDao = new AccountDaoImpl(storage);

    @Test
    public void shouldReturnAccountWhenExistsTest() {
        Account dummyAccount = new Account();
        dummyAccount.setBalance(BigDecimal.valueOf(123.45));
        dummyAccount.setId("existingAccountId");
        storage.put(dummyAccount.getId(), dummyAccount);
        accountDao = new AccountDaoImpl(storage);
        assertNotNull(accountDao.getAccountById("existingAccountId"));
    }

    @Test
    public void shouldReturnNullWhenAccountNotExist() {
        assertNull(accountDao.getAccountById("nonexistentAccountId"));
    }

    @Test
    public void shouldPersistAccountWhenCreateInvoked() {
        Account dummyAccount = new Account();
        dummyAccount.setBalance(BigDecimal.valueOf(123.45));
        dummyAccount.setId("existingAccountId");
        accountDao.create(dummyAccount);
        assertEquals(dummyAccount, storage.get(dummyAccount.getId()));
    }

    @Test
    public void shouldUpdateAccountWhenExists() {
        Account initialAccount = new Account();
        initialAccount.setId("account-id");
        initialAccount.setBalance(BigDecimal.valueOf(1));
        storage.put(initialAccount.getId(), initialAccount);

        Account modifiedAccount = new Account();
        modifiedAccount.setId("account-id");
        modifiedAccount.setBalance(BigDecimal.valueOf(2));

        accountDao.update(modifiedAccount);

        assertEquals(1, storage.size());
        assertTrue(storage.containsKey(modifiedAccount.getId()));
        assertEquals(modifiedAccount, storage.get(modifiedAccount.getId()));
    }

    @Test
    public void shouldNotUpdateAccountWhenNotExists() {
        Account account = new Account();
        account.setId("account-id");
        account.setBalance(BigDecimal.valueOf(1));

        accountDao.update(account);

        assertFalse(storage.containsKey(account.getId()));
    }

    @Test
    public void shouldReturnListOfAccountsWhenInvoked() {
        Account firstAccount = new Account();
        firstAccount.setId("first");
        firstAccount.setBalance(BigDecimal.valueOf(123.45));
        storage.put(firstAccount.getId(), firstAccount);

        Account secondAccount = new Account();
        secondAccount.setId("second");
        secondAccount.setBalance(BigDecimal.valueOf(123.45));
        storage.put(secondAccount.getId(), secondAccount);

        List<Account> accounts = accountDao.getAccounts();

        assertEquals(2, accounts.size());
        assertTrue(accounts.contains(firstAccount));
        assertTrue(accounts.contains(secondAccount));
    }

}