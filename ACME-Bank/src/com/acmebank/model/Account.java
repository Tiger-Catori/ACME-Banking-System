package com.acmebank.model;
public abstract class Account {
    // The blueprint for all bank accounts.
    // No real account of this type will exist.
    // Just defines common fields and methods.

    // Variables
    private int accountNumber;
    private int sortCode;
    private double currentBalance;

    // Constructor
    public Account(double currentBalance, int sortCode) {
        // Setting random 8 digit number in the constructor.
        this.accountNumber = (int) (Math.random() * 90_000_000) + 10_000_000;
        this.sortCode = sortCode;
        // Rounding to 2 decimal places.
        this.currentBalance = (Math.round(currentBalance * 100.00) / 100.00);
    }

    // Abstract Methods
    public abstract double deposit(double amount);
    public abstract double withdraw(double amount);
    public abstract double calculateInterest();

    // Concrete Methods (Getters)
    public double getBalance() {
        return currentBalance;
    }
    public int getAccountNumber() {
        return accountNumber;
    }
    public int getSortCode() {
        return sortCode;
    }

    // Setter for subclasses to safely change balance.
    public void setBalance(double amount) {
        this.currentBalance = amount;
    }

    //\
    public void displayDetails(){
        System.out.println(
                "These are your account details: " +
                        "\nAccount Number: " + accountNumber +
                        "\nSortCode: " + sortCode +
                        "\nBalance: " + currentBalance

        );
    }
}
