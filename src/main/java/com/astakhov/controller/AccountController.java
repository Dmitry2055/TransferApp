package com.astakhov.controller;

import com.astakhov.dto.AccountListResponse;
import com.astakhov.dto.CreateAccountRequest;
import com.astakhov.dto.ErrorResponse;
import com.astakhov.entity.Account;
import com.astakhov.service.AccountService;
import com.astakhov.util.ConverterUtils;
import spark.Request;
import spark.Response;

import java.util.List;
import java.util.Optional;

import static java.net.HttpURLConnection.*;

/**
 * Controller for handling accounts.
 */
public class AccountController {
    private final AccountService accountService;

    public AccountController(final AccountService accountService) {
        this.accountService = accountService;
    }

    public Object createAccount(final Request request, final Response response) {
        CreateAccountRequest createAccountRequest = ConverterUtils.toObject(request.body(), CreateAccountRequest.class);
        Account createdAccount = accountService.createAccount(createAccountRequest.getBalance());
        response.status(HTTP_CREATED);
        return ConverterUtils.toJson(createdAccount);
    }

    public Object getAccount(final Request request, final Response response) {
        String accountId = request.params("id");
        Optional<Account> account = accountService.getAccount(accountId);
        if (account.isPresent()) {
            response.status(HTTP_OK);
            return ConverterUtils.toJson(account.get());
        } else {
            response.status(HTTP_NOT_FOUND);
            ErrorResponse error = new ErrorResponse();
            error.setMessage(String.format("Account %s not found", accountId));
            return ConverterUtils.toJson(error);
        }
    }

    public Object getAccounts(final Request request, final Response response) {
        List<Account> accounts = accountService.getAccounts();
        AccountListResponse accountList = new AccountListResponse();
        accountList.setItems(accounts);
        response.status(HTTP_OK);
        return ConverterUtils.toJson(accountList);
    }
}
