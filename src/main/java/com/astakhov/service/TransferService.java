package com.astakhov.service;

import com.astakhov.entity.Transfer;

public interface TransferService {

    /**
     * Transfer specified amount from one account to another.
     * 1. Accounts must exist
     * 2. Amount transferred must not exceed available amount
     * 3. Amount should not be negative
     *
     * @param transfer a transfer to make
     */
    void performTransfer(Transfer transfer);
}
