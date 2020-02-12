package com.astakhov.service;

import com.astakhov.entity.Account;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface AccountService {

    void updateAccount(Account account);

    Account createAccount(BigDecimal balance);

    Optional<Account> getAccount(String id);

    List<Account> getAccounts();
}
