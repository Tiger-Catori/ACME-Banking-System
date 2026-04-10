package com.acmebank.model;

import java.util.ArrayList;
import java.util.List;

public abstract class Account {
    // The blueprint for all bank accounts.
    // No real account of this type will exist.
    // Just defines common fields and methods.

    // Variables
    private AccountNumber accountNumber;
    private SortCode sortCode;
    private double currentBalance;
    private final List<Transaction> transactionHistory;

    // Constructor
    public Account(double currentBalance, SortCode sortCode) {
        // Setting random 8 digit number in the constructor.
        int randomNum = (int) (Math.random() * 90_000_000) + 10_000_000;
        this.accountNumber = new AccountNumber(String.valueOf(randomNum));
        this.sortCode = sortCode;
        this.currentBalance = Math.round(currentBalance * 100.00) / 100.00;
        this.transactionHistory = new ArrayList<>();
    }

    // Abstract Methods
    public abstract double deposit(double amount);
    public abstract double withdraw(double amount);
    public abstract double calculateInterest();

    // Concrete Methods (Getters)
    public double getBalance() {
        return currentBalance;
    }

    public AccountNumber getAccountNumber() {
        return accountNumber;
    }

    public SortCode getSortCode() {
        return sortCode;
    }

    public List<Transaction> getTransactionHistory() {
        return new ArrayList<>(transactionHistory);
    }

    // Setter for subclasses to safely change balance.
    public void setBalance(double amount) {
        this.currentBalance = amount;
    }

    public void addTransaction(Transaction transaction) {
        transactionHistory.add(transaction);
    }

    //\
    public void displayDetails(){
        System.out.println(
                "These are your account details: " +
                        "\nAccount Number: " + getAccountNumber() +
                        "\nSortCode: " + getSortCode() +
                        "\nBalance: £" + getBalance()

        );
    }
}