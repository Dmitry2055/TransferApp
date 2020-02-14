package com.astakhov.controller;

import com.astakhov.entity.Transfer;
import com.astakhov.service.TransferService;
import com.astakhov.util.ConverterUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;

import static java.net.HttpURLConnection.HTTP_CREATED;

/**
 * Controller for handling transfers.
 */
public class TransferController {
    private static final Logger LOG = LoggerFactory.getLogger(TransferController.class);

    private final TransferService transferService;

    public TransferController(final TransferService transferService) {
        this.transferService = transferService;
    }

    public Object makeTransfer(final Request request, final Response response) {
        Transfer transfer = ConverterUtils.toObject(request.body(), Transfer.class);
        transferService.performTransfer(transfer);
        response.status(HTTP_CREATED);
        return "";
    }
}
