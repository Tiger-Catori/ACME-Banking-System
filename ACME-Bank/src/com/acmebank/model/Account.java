package com.acmebank.model;

public abstract class Account {
    // The blueprint for all bank accounts.
    // No real account of this type will exist.
    // Just defines common fields and methods.

    // Variables
    private int accountNumber;
    private int sortCode;
    private double currentBalance;


    // Abstract Methods
    public abstract double deposit();
    public abstract double withdraw();
    public abstract double calculateInterest();

    // Concrete Methods
//    public double getBalance() {}
//    public int getAccountNumber() {}
//    public int getSortCode() {}
//    public void displayDetails(){}
}
