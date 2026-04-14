package com.acmebank.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import com.acmebank.exceptions.InsufficientFundsException;

public class IsaAccount extends Account {
    public IsaAccount(double currentBalance) {
        super(currentBalance, SortCode.from(60,60,70));
    }

    // Also using factory pattern to create class.
    public static IsaAccount create(double amount) {
        return new IsaAccount(amount);
    }


    @Override
    public double deposit(double amount) {
        if (amount >= 0) {
            setBalance(getBalance() + amount);
        }
        return getBalance();
    }

    @Override
    public double withdraw(double amount) throws InsufficientFundsException {
        if (getBalance() - amount >= 0) {
            setBalance(getBalance() - amount);
        } else {
            throw new InsufficientFundsException("Insufficient Funds: £"+ String.format("%.2f", getBalance()) +
                    ", Attempted: £" + String.format("%.2f", amount));
        }
        return getBalance();
    }

    @Override
    public double calculateInterest() {
        double balance = getBalance();
        double interestRate = 0.0275; // 2.75% APR
        double interest = balance * interestRate;
        // Round to 2 decimal places
        BigDecimal roundedInterest = new BigDecimal(interest).setScale(2, RoundingMode.HALF_UP);
        return roundedInterest.doubleValue();
    }
}