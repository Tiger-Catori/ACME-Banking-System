package com.acmebank.infrastructure.generation;

import com.acmebank.model.AccountNumber;
import com.acmebank.exceptions.DuplicateAccountNumberException;
import java.util.HashSet;
import java.util.Set;

public class AccountNumberGenerator {
    private static final Set<String> usedNumbers = new HashSet<>();

    // Generates a unique random 8-digit number and automatically registers it.
    public static AccountNumber generate() {
        String randomNum;
        do {
            int num = (int) (Math.random() * 90_000_000) + 10_000_000;
            randomNum = String.valueOf(num);
        } while (usedNumbers.contains(randomNum));

        // Add the number to the set of used numbers
        usedNumbers.add(randomNum);
        return new AccountNumber(randomNum);
    }

    // Registers an existing account number (e.g., when loading from JSON)
    public static void registerExistingNumber(AccountNumber accountNumber) throws DuplicateAccountNumberException {
        if (accountNumber != null) {
            if (!usedNumbers.add(accountNumber.getValue())) {
                throw new DuplicateAccountNumberException("Account Number " + accountNumber.getValue() + " already exists.");
            }
        }
    }

    // Removes an account number (e.g., if closing an account)
    public static void unregisterNumber(AccountNumber accountNumber) {
        if (accountNumber != null) {
            usedNumbers.remove(accountNumber.getValue());
        }
    }

    // Clears all tracked numbers (for testing)
    public static void reset() {
        usedNumbers.clear();
    }
}