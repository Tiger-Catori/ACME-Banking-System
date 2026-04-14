package com.acmebank.model;
import com.acmebank.exceptions.InsufficientFundsException;

public class PersonalAccount extends Account {
    public PersonalAccount(double currentBalance) {
        super(currentBalance, SortCode.from(60,60,60));
    }

    // Using Factory Pattern to only create PersonalAccount objects
    // if its balance is greater than 1.
    // To Enforce £1 minimum opening balance.
    public static PersonalAccount create(double amount) {
       if (amount >= 1) { // Create new PersonalAccount object.
           return new PersonalAccount(amount);
       } else {
           return null; // Don't create new object
       }
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
        // Set an overdraft of £500.
        int overdraft = 500;
        if (getBalance() - amount >= -overdraft) throw new InsufficientFundsException( "Insufficient funds. Balance: £"
                + String.format("%.2f", getBalance()) + ", Attempted: £" + String.format("%.2f", amount)){
        };
        return getBalance();
    }

    @Override
    public double calculateInterest() {
        return 0;
    }
}