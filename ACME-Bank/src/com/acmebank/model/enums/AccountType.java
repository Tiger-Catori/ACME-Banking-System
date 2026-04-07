package com.acmebank.model.enums;

import com.acmebank.model.Account;

public enum AccountType {
    // Represent the type of bank account
    PERSONAL("Personal Account"),
    ISA("ISA Account"),
    BUSINESS("Business Account");

    private final String displayName;

    AccountType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return getDisplayName();
    }
}
