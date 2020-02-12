package com.astakhov.service.impl;

import com.astakhov.exception.ServiceException;
import com.astakhov.entity.Account;
import com.astakhov.entity.Transfer;
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
        if (transfer.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            throw new ServiceException("Cannot complete transfer: the amount cannot be negative");
        }

        final Account fromAccount = accountService.getAccount(transfer.getSourceAccountId())
                .orElseThrow(() -> this.handleAbsentAccount(transfer.getSourceAccountId()));
        final Account toAccount = accountService.getAccount(transfer.getDestinationAccountId())
                .orElseThrow(() -> this.handleAbsentAccount(transfer.getDestinationAccountId()));

        if (fromAccount.getBalance().compareTo(transfer.getAmount()) < 0) {
            throw new ServiceException("Insufficient balance");
        }

        synchronized (this) {
            /* start transaction */
            toAccount.setBalance(toAccount.getBalance().add(transfer.getAmount()));
            fromAccount.setBalance(fromAccount.getBalance().subtract(transfer.getAmount()));
            accountService.updateAccount(toAccount);
            accountService.updateAccount(fromAccount);
            /* end transaction */
        }

    }

    private ServiceException handleAbsentAccount(final String accountId) {
        return new ServiceException(String.format("Cannot complete transfer: account %s does not exist", accountId));
    }
}
