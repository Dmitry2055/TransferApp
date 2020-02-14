package com.astakhov.service.impl;

import com.astakhov.exception.ServiceException;
import com.astakhov.dao.AccountDao;
import com.astakhov.entity.Account;
import com.google.common.collect.Lists;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AccountServiceImplUnitTest {

    @Mock
    private AccountDao accountDao;

    @InjectMocks
    private AccountServiceImpl service;

    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    @Test
    public void shouldCreateAccountWhenInvoked() {
        BigDecimal balance = BigDecimal.valueOf(123.45);
        Account createdAccount = service.createAccount(balance);
        assertNotNull(createdAccount);
        verify(accountDao, times(1)).create(createdAccount);
    }

    @Test
    public void shouldThrowWhenCreatingAccountWithNegativeBalance() {
        thrown.expect(ServiceException.class);
        thrown.expectMessage("Cannot create an account with negative balance");
        BigDecimal balance = BigDecimal.valueOf(-123.45);
        service.createAccount(balance);
    }

    @Test
    public void shouldReturnAccountWhenExists() {
        Account expectedAccount = new Account();
        expectedAccount.setId("account-id");
        expectedAccount.setBalance(BigDecimal.valueOf(123.45));

        when(accountDao.getAccountById("account-id")).thenReturn(expectedAccount);

        Optional<Account> account = service.getAccount("account-id");

        assertTrue(account.isPresent());
        assertEquals(expectedAccount, account.get());
    }

    @Test
    public void shouldReturnEmptyOptionalOfAccountWhenNotExist() {
        Optional<Account> account = service.getAccount("account-id");
        assertTrue(account.isEmpty());
    }

    @Test
    public void shouldReturnListOfAccountsWhenExist() {
        Account firstAccount = new Account();
        firstAccount.setId("first");
        firstAccount.setBalance(BigDecimal.valueOf(123.45));

        Account secondAccount = new Account();
        secondAccount.setId("second");
        secondAccount.setBalance(BigDecimal.valueOf(123.45));

        when(accountDao.getAccounts()).thenReturn(Lists.newArrayList(firstAccount, secondAccount));

        List<Account> accounts = service.getAccounts();

        assertEquals(2, accounts.size());
        assertTrue(accounts.contains(firstAccount));
        assertTrue(accounts.contains(secondAccount));
    }

    @Test
    public void shouldUpdateAccountWhenInvoked() {
        Account account = new Account();
        account.setId("account-id");
        account.setBalance(BigDecimal.valueOf(123.45));

        service.updateAccount(account);

        verify(accountDao, times(1)).update(account);
    }
}