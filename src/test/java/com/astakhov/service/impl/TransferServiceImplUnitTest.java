package com.astakhov.service.impl;

import com.astakhov.exception.ServiceException;
import com.astakhov.entity.Account;
import com.astakhov.entity.Transfer;
import com.astakhov.service.AccountService;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TransferServiceImplUnitTest {

    @Mock
    private AccountService accountService;

    @InjectMocks
    private TransferServiceImpl service;

    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    @Test
    public void shouldThrowWhenTransferringNegativeAmount() {
        thrown.expect(ServiceException.class);
        thrown.expectMessage("Cannot transfer negative amount");
        Transfer negativeAmountTransfer = new Transfer();
        negativeAmountTransfer.setAmount(BigDecimal.valueOf(-123.45));
        negativeAmountTransfer.setSourceAccountId("source");
        negativeAmountTransfer.setDestinationAccountId("destination");
        service.performTransfer(negativeAmountTransfer);
    }

    @Test
    public void shouldThrowWhenInsufficientFundsOnDestinationAccount() {
        thrown.expect(ServiceException.class);
        thrown.expectMessage("Insufficient balance");

        Account sourceAccount = new Account();
        sourceAccount.setId("source");
        sourceAccount.setBalance(BigDecimal.valueOf(0));

        Account destinationAccount = new Account();
        destinationAccount.setId("destination");
        destinationAccount.setBalance(BigDecimal.valueOf(123.45));

        when(accountService.getAccount("source")).thenReturn(Optional.of(sourceAccount));
        when(accountService.getAccount("destination")).thenReturn(Optional.of(destinationAccount));
        Transfer transfer = new Transfer();
        transfer.setAmount(BigDecimal.valueOf(1.0));
        transfer.setSourceAccountId("source");
        transfer.setDestinationAccountId("destination");
        service.performTransfer(transfer);
    }

    @Test
    public void shouldUpdateAccountsWhenTransferInvoked() {
        Account sourceAccount = new Account();
        sourceAccount.setId("source");
        sourceAccount.setBalance(BigDecimal.valueOf(10));

        Account destinationAccount = new Account();
        destinationAccount.setId("destination");
        destinationAccount.setBalance(BigDecimal.valueOf(10));

        when(accountService.getAccount("source")).thenReturn(Optional.of(sourceAccount));
        when(accountService.getAccount("destination")).thenReturn(Optional.of(destinationAccount));
        Transfer transfer = new Transfer();
        transfer.setAmount(BigDecimal.valueOf(1.0));
        transfer.setSourceAccountId("source");
        transfer.setDestinationAccountId("destination");
        service.performTransfer(transfer);

        verify(accountService, times(1)).updateAccount(sourceAccount);
        verify(accountService, times(1)).updateAccount(destinationAccount);
    }

}
