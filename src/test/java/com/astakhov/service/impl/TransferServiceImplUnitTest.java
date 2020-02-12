package com.astakhov.service.impl;

import com.astakhov.entity.Account;
import com.astakhov.entity.Transfer;
import com.astakhov.exception.ServiceException;
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
    private TransferServiceImpl transferService;

    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    @Test
    public void shouldThrowWhenTransferringNegativeAmount() {
        Transfer negativeAmountTransfer = new Transfer();
        negativeAmountTransfer.setAmount(BigDecimal.valueOf(-123.45));
        negativeAmountTransfer.setSourceAccountId("source");
        negativeAmountTransfer.setDestinationAccountId("destination");

        thrown.expect(ServiceException.class);
        thrown.expectMessage("Cannot complete transfer: the amount cannot be negative");

        transferService.performTransfer(negativeAmountTransfer);
    }

    @Test
    public void shouldThrowWhenInsufficientFundsOnDestinationAccount() {
        Account sourceAccount = new Account();
        sourceAccount.setId("source");
        sourceAccount.setBalance(BigDecimal.valueOf(0));

        Account destinationAccount = new Account();
        destinationAccount.setId("destination");
        destinationAccount.setBalance(BigDecimal.valueOf(123.45));

        Transfer transfer = new Transfer();
        transfer.setAmount(BigDecimal.valueOf(1.0));
        transfer.setSourceAccountId("source");
        transfer.setDestinationAccountId("destination");

        thrown.expect(ServiceException.class);
        thrown.expectMessage("Cannot complete transfer: insufficient funds on 'source'");
        when(accountService.getAccount("source")).thenReturn(Optional.of(sourceAccount));
        when(accountService.getAccount("destination")).thenReturn(Optional.of(destinationAccount));

        transferService.performTransfer(transfer);
    }

    @Test
    public void shouldUpdateAccountsWhenTransferInvoked() {
        Account sourceAccount = new Account();
        sourceAccount.setId("source");
        sourceAccount.setBalance(BigDecimal.valueOf(10));

        Account destinationAccount = new Account();
        destinationAccount.setId("destination");
        destinationAccount.setBalance(BigDecimal.valueOf(10));

        Transfer transfer = new Transfer();
        transfer.setAmount(BigDecimal.valueOf(1.0));
        transfer.setSourceAccountId("source");
        transfer.setDestinationAccountId("destination");

        when(accountService.getAccount("source")).thenReturn(Optional.of(sourceAccount));
        when(accountService.getAccount("destination")).thenReturn(Optional.of(destinationAccount));

        transferService.performTransfer(transfer);

        verify(accountService, times(1)).updateAccount(sourceAccount);
        verify(accountService, times(1)).updateAccount(destinationAccount);
    }

    @Test
    public void shouldThrowWhenSourceAccountNotExist() {
        Transfer transfer = new Transfer();
        transfer.setAmount(BigDecimal.valueOf(1.0));
        transfer.setSourceAccountId("nonexistent-source");
        transfer.setDestinationAccountId("destination");

        thrown.expect(ServiceException.class);
        thrown.expectMessage("Cannot complete transfer: account 'nonexistent-source' does not exist");
        when(accountService.getAccount("nonexistent-source")).thenReturn(Optional.empty());

        transferService.performTransfer(transfer);
    }

    @Test
    public void shouldThrowWhenDestinationAccountNotExist() {
        Account sourceAccount = new Account();
        sourceAccount.setId("source");
        sourceAccount.setBalance(BigDecimal.valueOf(10));

        Transfer transfer = new Transfer();
        transfer.setAmount(BigDecimal.valueOf(1.0));
        transfer.setSourceAccountId("source");
        transfer.setDestinationAccountId("nonexistent-destination");

        thrown.expect(ServiceException.class);
        thrown.expectMessage("Cannot complete transfer: account 'nonexistent-destination' does not exist");
        when(accountService.getAccount("source")).thenReturn(Optional.of(sourceAccount));

        transferService.performTransfer(transfer);
    }

}
