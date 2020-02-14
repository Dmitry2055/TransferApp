package com.astakhov.service.impl;

import com.astakhov.entity.Account;
import com.astakhov.entity.Transfer;
import com.astakhov.exception.ServiceException;
import com.astakhov.service.AccountService;
import com.astakhov.service.TransferService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;


public class TransferServiceImpl implements TransferService {

    private static final Logger LOG = LoggerFactory.getLogger(TransferServiceImpl.class);
    private final AccountService accountService;

    public TransferServiceImpl(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public void performTransfer(final Transfer transfer) {
        LOG.info("Transferring {} from '{}' to '{}'",
                transfer.getAmount(), transfer.getSourceAccountId(), transfer.getDestinationAccountId());

        validateTransfer(transfer);

        final Account fromAccount = accountService.getAccount(transfer.getSourceAccountId())
                .orElseThrow(() -> this.handleAbsentAccount(transfer.getSourceAccountId()));
        final Account toAccount = accountService.getAccount(transfer.getDestinationAccountId())
                .orElseThrow(() -> this.handleAbsentAccount(transfer.getDestinationAccountId()));


        synchronized (this) {
            /* start transaction */
            verifySufficientFunds(fromAccount, transfer.getAmount());
            toAccount.setBalance(toAccount.getBalance().add(transfer.getAmount()));
            fromAccount.setBalance(fromAccount.getBalance().subtract(transfer.getAmount()));
            accountService.updateAccount(toAccount);
            accountService.updateAccount(fromAccount);
            /* end transaction */
        }
        LOG.info("Transfer {} from '{}' to '{}' successful",
                transfer.getAmount(), transfer.getSourceAccountId(), transfer.getDestinationAccountId());
    }

    private void verifySufficientFunds(final Account account, final BigDecimal amount) {
        if (account.getBalance().compareTo(amount) < 0) {
            throw new ServiceException(String.format("Cannot complete transfer: insufficient funds on '%s'", account.getId()));
        }
    }

    private void validateTransfer(final Transfer transfer) {
        if (BigDecimal.ZERO.compareTo(transfer.getAmount()) >= 0) {
            throw new ServiceException("Cannot complete transfer: the amount must be positive");
        }
    }

    private ServiceException handleAbsentAccount(final String accountId) {
        return new ServiceException(String.format("Cannot complete transfer: account '%s' does not exist", accountId));
    }
}
