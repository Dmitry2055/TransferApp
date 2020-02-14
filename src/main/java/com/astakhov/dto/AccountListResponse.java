package com.astakhov.dto;

import com.astakhov.entity.Account;

import java.util.List;

public class AccountListResponse {
    private List<Account> items;

    public List<Account> getItems() {
        return items;
    }

    public void setItems(List<Account> items) {
        this.items = items;
    }
}
