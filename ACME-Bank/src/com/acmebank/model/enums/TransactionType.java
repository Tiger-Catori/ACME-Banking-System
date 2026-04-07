package com.acmebank.model.enums;

public enum TransactionType {
    // Type of financial transaction.
    // Used to obtain transaction history and for audit logging

    DEPOSIT("Deposit"),
    WITHDRAWAL("Withdrawal"),
    TRANSFER("Transfer"),
    FEE("Fee"),
    INTEREST("Interest");

    public final String displayName;

    TransactionType(String displayName) {
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
