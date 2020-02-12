package com.astakhov.service;

import com.astakhov.entity.Transfer;

/**
 * Service for transfer-related functionality.
 */
public interface TransferService {

    /**
     * Perform a transfer described by passed {@link Transfer} object.
     *
     * @param transfer a transfer to make
     */
    void performTransfer(Transfer transfer);
}
