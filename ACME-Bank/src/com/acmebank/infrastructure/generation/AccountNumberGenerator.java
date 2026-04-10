package com.acmebank.infrastructure.generation;

import com.acmebank.model.AccountNumber;

import java.util.HashSet;
import java.util.Set;

public class AccountNumberGenerator {
    private static final Set<String> usedNumbers = new HashSet<>();

    // Generates a unique random 8-digit number
    // returns AccountNumber object with that number.
    public static AccountNumber generate() {
        String randomNum;

        do {
            int num = (int) (Math.random() * 90_000_000) + 10_000_000;
            randomNum = String.valueOf(num);
        } while (usedNumbers.contains(randomNum));

        return new AccountNumber(randomNum);
    }

    // registers an existing account number,
    // prevents future generation of the same number.
    public static void registerExistingNumber(AccountNumber accountNumber) {
        if (accountNumber != null) {
            usedNumbers.add(accountNumber.getValue());
        }
    }

    // Removes an account number (e.g if closing an account)
    public static void unregisterNumber(AccountNumber accountNumber) {
        if (accountNumber != null) {
            usedNumbers.remove(accountNumber.getValue());
        }
    }

    // Clears all tracked numbers
    public static void reset() {
        usedNumbers.clear();
    }
}
