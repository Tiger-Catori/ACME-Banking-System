package com.acmebank.model;

public class PersonalAccount extends Account {

    private static final int SORT_CODE = 606060;

    public PersonalAccount(double currentBalance) {
        super(currentBalance, SORT_CODE);
    }

    @Override
    public double deposit(double amount) {
        if (amount >= 0) {
            setBalance(getBalance() + amount);
        }
        return getBalance();
    }

    @Override
    public double withdraw(double amount) {
        // Set an overdraft of 500.
        int overdraft = 500;
        if (getBalance() - amount >= -500) {
            setBalance(getBalance() - amount);
        }
        return getBalance();
    }

    @Override
    public double calculateInterest() {
        return 0;
    }
}