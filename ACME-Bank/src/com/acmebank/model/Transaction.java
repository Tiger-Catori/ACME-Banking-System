package com.acmebank.model;

import com.acmebank.model.enums.TransactionType;
import java.time.LocalDateTime;

// This class is an immutable record of a single financial event.


public final class Transaction {
    private LocalDateTime timestamp;
    private TransactionType  transactionType;
    private String description;
    private double amount;
    // amount is stored as a positive number.
    // The type tells you if it was added or subtracted.
    private double balanceAfter;

    public Transaction(
            LocalDateTime timestamp,
            TransactionType transactionType,
            String description,
            double amount,
            double balanceAfter) {
        this.timestamp = timestamp;
        this.transactionType = transactionType;
        this.description = description;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
    }

    // Factory method object creation.
    public static Transaction create(LocalDateTime timestamp, TransactionType transactionType, String description, double amount, double balanceAfter) {
        return new Transaction(timestamp, transactionType, description, amount, balanceAfter);
    }


    // Getters
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public TransactionType getType() {
        return transactionType;
    }

    public String getDescription() {
        return description;
    }

    public double getAmount() {
        return amount;
    }

    public double getBalanceAfter() {
        return balanceAfter;
    }

    @Override
    public String toString() {
        String formattedString =
                "Date & Time: " + getTimestamp() + "\n" +
                        transactionType.getDisplayName() + ": " + getAmount() + "\n" +
                        "Balance After: " + getBalanceAfter() + "\n" +
                        "Desciption: " + getDescription();

        return formattedString;
    }


}