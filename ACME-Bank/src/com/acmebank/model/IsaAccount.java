package com.acmebank.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class IsaAccount extends Account {
    private static final int SORT_CODE = 606070;

    public IsaAccount(double currentBalance) {
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
        if (getBalance() - amount > 0) {
            setBalance(getBalance() - amount);
        }
        return getBalance();
    }

    @Override
    public double calculateInterest() {
        double balance = getBalance();
        double interest = 1.0275;

        // Rounding to 2 dp with BigDecimal class
        BigDecimal roundedNumber = new BigDecimal(interest * balance).setScale(2, RoundingMode.HALF_UP);

        return roundedNumber.doubleValue();
    }
}
