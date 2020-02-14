package com.astakhov;

import com.astakhov.controller.AccountController;
import com.astakhov.controller.TransferController;
import com.astakhov.dao.AccountDao;
import com.astakhov.dao.impl.AccountDaoImpl;
import com.astakhov.exception.ServiceException;
import com.astakhov.service.AccountService;
import com.astakhov.service.TransferService;
import com.astakhov.service.impl.AccountServiceImpl;
import com.astakhov.service.impl.TransferServiceImpl;
import com.astakhov.util.HandlerUtils;
import spark.Spark;

import java.util.HashMap;

public class Application {
    public static void main(String[] args) {
        AccountDao accountDao = new AccountDaoImpl(new HashMap<>());
        AccountService accountService = new AccountServiceImpl(accountDao);
        TransferService transferService = new TransferServiceImpl(accountService);
        TransferController transferController = new TransferController(transferService);
        AccountController accountController = new AccountController(accountService);

        Spark.before((request, response) -> response.type("application/json"));
        Spark.exception(ServiceException.class, HandlerUtils::handleServiceException);
        Spark.notFound(HandlerUtils::notFound);
        Spark.internalServerError(HandlerUtils::internalServerError);

        Spark.post("/transfers", transferController::makeTransfer);
        Spark.post("/accounts", accountController::createAccount);
        Spark.get("/accounts", accountController::getAccounts);
        Spark.get("/accounts/:id", accountController::getAccount);
    }
}
