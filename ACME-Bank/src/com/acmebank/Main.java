package com.acmebank;

import com.acmebank.cli.MenuController;
import com.acmebank.cli.handlers.InputHandler;
import com.acmebank.infrastructure.generation.AccountNumberGenerator;
import com.acmebank.infrastructure.logging.AuditLogger;
import com.acmebank.infrastructure.logging.FileAuditLogger;
import com.acmebank.infrastructure.persistance.DataPersistance;
import com.acmebank.infrastructure.persistance.JsonPersistance;
import com.acmebank.model.Account;
import com.acmebank.model.Customer;
import com.acmebank.model.PersonalAccount;
import com.acmebank.service.*;
import com.acmebank.exceptions.DuplicateAccountNumberException;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        // 1. Setup logging and persistence
        AuditLogger auditLogger = new FileAuditLogger("data/audit.log");
        DataPersistance dataPersistence = new JsonPersistance("data/customers.json", auditLogger);

        // 2. Load existing customers
        List<Customer> allCustomers = dataPersistence.loadCustomers();

        // 2b. if no customers exists
        String startupMessage = null;
        if (allCustomers.isEmpty()) {
            Customer defaultCustomer = Customer.create("John", "Doe");
            PersonalAccount account = PersonalAccount.create(240.356);

            if (account != null) {
                try {
                    defaultCustomer.addAccount(account);
                } catch (Exception e) {
                    System.err.println("Error adding account to default customer: " + e.getMessage());
                }
            } else {
                System.err.println("Failed to create default account (balance must be ≥1).");
            }

            allCustomers.add(defaultCustomer);
            dataPersistence.saveCustomers(allCustomers);

            StringBuilder sb = new StringBuilder();
            sb.append("\n==========================================\n");
            sb.append("TEST CUSTOMER CREATED\n");
            sb.append("==========================================\n");
            sb.append("Name: John Doe\n");
            sb.append("Customer ID: ").append(defaultCustomer.getCustomerID()).append("\n");
            if (account != null) {
                sb.append("Account Number: ").append(account.getAccountNumber().getValue()).append("\n");
                sb.append("Initial Balance: £").append(String.format("%.2f", account.getBalance())).append("\n");
            }
            sb.append("Use this Customer ID to log in.\n");
            sb.append("==========================================\n");

            startupMessage = sb.toString();
        }

        // 3. Register all existing account numbers – skip if already registered
        for (Customer customer : allCustomers) {
            for (Account account : customer.getAccounts()) {
                try {
                    AccountNumberGenerator.registerExistingNumber(account.getAccountNumber());
                } catch (DuplicateAccountNumberException e) {
                    // Already registered (e.g., from generation during this run), ignore
                }
            }
        }

        // 4. Create service instances
        BusinessAccountValidator businessValidator = new BusinessAccountValidator();
        FeeService feeService = new FeeService();
        AccountNumberGenerator accountNumberGenerator = new AccountNumberGenerator();

        AuthService authService = new AuthService(allCustomers);

        AccountService accountService = new AccountService(
                accountNumberGenerator,
                businessValidator,
                auditLogger,
                dataPersistence,
                feeService,
                allCustomers
        );

        TransactionService transactionService = TransactionService.create(
                auditLogger,
                dataPersistence,
                allCustomers
        );

        // 5. CLI helpers
        InputHandler inputHandler = new InputHandler();

        // 6. Create and start menu controller
        MenuController controller = new MenuController(
                authService,
                accountService,
                transactionService,
                auditLogger,
                dataPersistence,
                allCustomers,
                inputHandler
        );

        if (startupMessage != null) {
            controller.setStartupMessage(startupMessage);
        }

        controller.start();
    }
}