package com.acmebank.model;

public class BusinessAccount extends Account {
    public static final int SORT_CODE = 607070;

    public BusinessAccount(double currentBalance) {
        super(currentBalance, SORT_CODE);
    }

    public static BusinessAccount create(double amount) {
        return new BusinessAccount(amount);
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
        int overdraft = 5000;
        if (getBalance() - amount >= -overdraft) {
            setBalance(getBalance() - amount);
        }
        return getBalance();
    }

    @Override
    public double calculateInterest() {
        return 0;
    }


}
