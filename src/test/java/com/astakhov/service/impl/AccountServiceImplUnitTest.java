package com.astakhov.service.impl;

import com.astakhov.exception.ServiceException;
import com.astakhov.dao.AccountDao;
import com.astakhov.entity.Account;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;

import static org.junit.Assert.assertNotNull;
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
        verify(accountDao, times(1)).save(createdAccount);
    }

    @Test
    public void shouldThrowWhenSavingInvalidAccount() {
        thrown.expect(ServiceException.class);
        thrown.expectMessage("Invalid account");
        BigDecimal balance = BigDecimal.valueOf(-123.45);
        Account createdAccount = service.createAccount(balance);
        verify(accountDao, never()).save(createdAccount);
    }

}