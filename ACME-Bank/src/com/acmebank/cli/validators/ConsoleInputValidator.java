package com.acmebank.cli.validators;

import com.acmebank.exceptions.InvalidAmountException;
import com.acmebank.model.Account;
import com.acmebank.model.enums.AccountType;

import java.util.Locale;

public class ConsoleInputValidator {

    // Validate that an amount is positive
    public static double validatePositiveAmount(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero.");
        }
        return amount;
    }

    // Validate and convert String to AccountType
    public static AccountType validateAccountType(String input) {
        String accountInput = input.trim().toUpperCase();
        switch (accountInput) {
            case "PERSONAL" -> {
                return AccountType.PERSONAL;
            }
            case "ISA" -> {
                return AccountType.ISA;
            }
            case "BUSINESS" -> {
                return AccountType.BUSINESS;
            }
            default -> throw new IllegalArgumentException("Invalid account type. Valid types: PERSONAL, ISA, BUSINESS");
        }
    }

    // Validate that is string is not empty or null.
    public static String validateNonEmpty(String input) {
        if (input == null || input.trim().isEmpty()) {
            throw new IllegalArgumentException("The input cannot be empty or null.");
        }
        return input.trim();
    }

    // Validate yes/no answer (returns true if yes)
    public static boolean validateYesNo(String input) {
        String inputString = input.trim().toLowerCase();
        if (inputString.equals("y") || inputString.equals("yes")) {
            return true;
        } else if (inputString.equals("n") || inputString.equals("no")) {
            return false;
        } else {
            throw new IllegalArgumentException("Please answer 'yes' or 'no'.");
        }
    }
}
