package com.acmebank;

import com.acmebank.exceptions.InsufficientFundsException;
import com.acmebank.exceptions.InvalidAmountException;
import com.acmebank.exceptions.InvalidBusinessTypeException;
import com.acmebank.infrastructure.generation.AccountNumberGenerator;
import com.acmebank.infrastructure.logging.AuditLogger;
import com.acmebank.infrastructure.logging.FileAuditLogger;
import com.acmebank.infrastructure.persistance.DataPersistance;
import com.acmebank.infrastructure.persistance.JsonPersistance;
import com.acmebank.model.*;
import com.acmebank.model.enums.BusinessType;
import com.acmebank.service.BusinessAccountValidator;
import com.acmebank.service.TransactionService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Main {

    public static void main(String[] args) {
        System.out.println("--- Testing Transaction ---");

        //\ 1. Setup dependencies
        AuditLogger logger = new FileAuditLogger("data/test.log");
        DataPersistance persistance = new JsonPersistance("data/persistance.json", logger);
        List<Customer> allCustomers = new ArrayList<>();

        // 2. Creating a test customer
        Customer customer = Customer.create("John", "Doe");
        System.out.println("Created customer: " + customer.getFirstName() + " "
                + customer.getLastName() +
                " (ID: " + customer.getCustomerID() + ")");

        // 3. Create two personal accounts
        PersonalAccount account1 = PersonalAccount.create(100.809);
        PersonalAccount account2 = PersonalAccount.create(50.665);

        if (account1 == null || account2 == null) {
            System.err.println("Failed to create accounts (minimum £1 needed). Exiting");
        }

        // 4. Add accounts to customer & register their numbers with generator
        try {
            customer.addAccount(account1);
            customer.addAccount(account2);
            AccountNumberGenerator.registerExistingNumber(account1.getAccountNumber());
            AccountNumberGenerator.registerExistingNumber(account2.getAccountNumber());
        } catch (Exception e) {
            System.err.println("Error adding accounts: " + e.getMessage());
        }
        allCustomers.add(customer);

        // 5. Creating TransactionService
        TransactionService txService = TransactionService.create(logger, persistance, allCustomers);

        // 6. Displaying initial balances
        System.out.println("\n--- Initial Balances ---");
        displayBalance(account1);
        displayBalance(account2);

        //\ 7. Test deposit
        System.out.println("\n--- Deposit £46 into Account 1 ---");
        try {
            txService.deposit(account1, 46.0);
            System.out.println("Deposit successful. New balance: £" + account1.getBalance());
        } catch (InvalidAmountException e) {
            System.err.println("Deposit failed: " + e.getMessage());
        }

        // 8. Testing withdrawal (valid)
        System.out.println("\n--- Withdraw £20 from Account 2 ---");
        try {
            txService.withdraw(account2, 20.0);
            System.out.println("Withdrawal successful. New balance: £" + account2.getBalance());
        } catch (InvalidAmountException | InsufficientFundsException e) {
            System.out.println("Withdrawal failed: " + e.getMessage());
        }

        // 9. Test withdrawal (insufficient funds)
        System.out.println("\n--- Withdraw £200 from Account2 (should fail) ---");
        try {
            txService.withdraw(account2, 200.0);
            System.out.println("Withdrawal successful. New balance: £" + account2.getBalance());
        } catch (InvalidAmountException | InsufficientFundsException e) {
            System.err.println("Withdrawal failed (expected) " + e.getMessage());
        }

        // 10. Testing transfer
        System.out.println("\n--- Transfer £25 from Account 1 to Account 2 ---");
        try {
            txService.transfer(account1, account2, 25.00);
            System.out.println("Transfer successful.");
        } catch (Exception e) {
            System.err.println("Transfer failed: " + e.getMessage());
        }

        // 11. Final balances
        System.out.println("\n--- Final balances ---");
        displayBalance(account1);
        displayBalance(account2);

        // 12. Showing transaction history for account1
        System.out.println("\n--- Transaction History for Account 1 ---");
        for (Transaction t : account1.getTransactionHistory()) {
            System.out.println(t);
            System.out.println("---------------------------");
        }

        // 13. Showing transaction history for account2
        System.out.println("\n--- Transaction History for Account 2 ---");
        for (Transaction t : account2.getTransactionHistory()) {
            System.out.println(t);
            System.out.println("---------------------------");
        }

        System.out.println("\nTest completed. Check data/test.log and data/testCustomers.json");

    }


    private static void displayBalance(Account account) {
        System.out.println("Account " + account.getAccountNumber().getValue() +
                " balance: £" + String.format("%.2f", account.getBalance()));
    }
}
