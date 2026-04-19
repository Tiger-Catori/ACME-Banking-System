package com.acmebank.cli.menus;

import com.acmebank.cli.MenuController;
import com.acmebank.cli.handlers.DisplayFormatter;
import com.acmebank.cli.handlers.InputHandler;
import com.acmebank.cli.validators.ConsoleInputValidator;
import com.acmebank.exceptions.InsufficientFundsException;
import com.acmebank.exceptions.InvalidAmountException;
import com.acmebank.model.Account;
import com.acmebank.model.Customer;

public class AccountOperationsMenu extends Menu {
    private final Account account;

    public AccountOperationsMenu(InputHandler inputHandler, Account account) {
        super(inputHandler);
        this.account = account;
    }

    @Override
    public void display(MenuController controller) {
        printTitle("Account Operations");

        Customer customer = controller.getCurrentCustomer();
        if (customer != null) {
            System.out.println("Customer: " + customer.getFirstName() + " "
                    + customer.getLastName() + " (ID " + customer.getCustomerID() + ")");
        }

        System.out.println(DisplayFormatter.formatAccountSummary(account));
        System.out.println("------------------------------------------");
        System.out.println("1. Deposit");
        System.out.println("2. Withdraw");
        System.out.println("3. Transfer");
        System.out.println("4. View Transaction History");
        System.out.println("5. View Account Details");
        System.out.println("6. Back to Customer Menu");
        System.out.println("==========================================");
    }

    @Override
    public void handleInput(MenuController controller, String choice) {
        switch (choice) {
            case "1" -> deposit(controller);
            case "2" -> withdraw(controller);
            case "3" -> transfer(controller);
            case "4" -> viewTransactionHistory();
            case "5" -> viewAccountDetails();
            case "6" -> {
                System.out.println("Returning to customer menu...");
                controller.switchToMenu(new CustomerMenu(inputHandler));
            }
            default -> printInvalidOptions();
        }
    }

    private void deposit(MenuController controller) {
        double amount = inputHandler.readDouble("Enter deposit amount: £");
        try {
            ConsoleInputValidator.validatePositiveAmount(amount);
            controller.getTransactionService().deposit(account, amount);
            controller.saveAllData();

            System.out.println("Deposit successful. New Balance: £"
                    + DisplayFormatter.formatBalance(account.getBalance()));

            inputHandler.readLine("Press Enter to continue...");
        } catch (InvalidAmountException | IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
            inputHandler.readLine("Press Enter to continue...");
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
            inputHandler.readLine("Press Enter to continue...");
        }
    }

    private void withdraw(MenuController controller) {
        double amount = inputHandler.readDouble("Enter withdrawal amount: £");
        try {
            ConsoleInputValidator.validatePositiveAmount(amount);
            controller.getTransactionService().withdraw(account, amount);
            controller.saveAllData();

            System.out.println("Withdrawal successful. New Balance: £"
                    + DisplayFormatter.formatBalance(account.getBalance()));

            inputHandler.readLine("Press Enter to continue...");
        } catch (InvalidAmountException | InsufficientFundsException e) {
            System.out.println("Error: " + e.getMessage());
            inputHandler.readLine("Press Enter to continue...");
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
            inputHandler.readLine("Press Enter to continue...");
        }
    }

    private void transfer(MenuController controller) {
        Customer currentCustomer = controller.getCurrentCustomer();
        if (currentCustomer == null) {
            System.out.println("Error: No customer authenticated.");
            inputHandler.readLine("Press Enter to continue...");
            return;
        }

        System.out.println("\nSelect destination account:");
        System.out.println(DisplayFormatter.formatAccountList(currentCustomer.getAccounts()));

        String accNumberStr = inputHandler.readLine("Enter destination account number: ");
        Account destination = null;

        for (Account acc : currentCustomer.getAccounts()) {
            if (acc.getAccountNumber().getValue().equals(accNumberStr)) {
                destination = acc;
                break;
            }
        }

        if (destination == null) {
            System.out.println("Error: Account not found.");
            inputHandler.readLine("Press Enter to continue...");
            return;
        }

        if (destination.equals(account)) {
            System.out.println("Error: Cannot transfer to the same account.");
            inputHandler.readLine("Press Enter to continue...");
            return;
        }

        double amount = inputHandler.readDouble("Enter transfer amount: £");

        try {
            ConsoleInputValidator.validatePositiveAmount(amount);
            controller.getTransactionService().transfer(account, destination, amount);
            controller.saveAllData();

            System.out.println("Transfer successful.");
            System.out.println("Source account new balance: £"
                    + DisplayFormatter.formatBalance(account.getBalance()));
            System.out.println("Destination account new balance: £"
                    + DisplayFormatter.formatBalance(destination.getBalance()));

            inputHandler.readLine("Press Enter to continue...");
        } catch (InvalidAmountException | InsufficientFundsException | IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
            inputHandler.readLine("Press Enter to continue...");
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
            inputHandler.readLine("Press Enter to continue...");
        }
    }

    private void viewTransactionHistory() {
        System.out.println("\n--- Transaction History ---");
        System.out.println(DisplayFormatter.formatTransactionHistory(account.getTransactionHistory()));
        inputHandler.readLine("Press Enter to continue...");
    }

    private void viewAccountDetails() {
        System.out.println("\n--- Account Details ---");
        System.out.println(DisplayFormatter.formattedAccountDetails(account));
        inputHandler.readLine("Press Enter to continue...");
    }

    @Override
    public String getHelpText() {
        return """
                ACCOUNT OPERATIONS HELP
                =======================
                1. Deposit - Add money to this account. Amount must be positive.
                2. Withdraw - Remove money from this account.
                   - Personal accounts may allow overdraft depending on business rules.
                   - ISA accounts may have withdrawal restrictions.
                   - Business accounts may require overdraft to be enabled.
                3. Transfer - Move money to another account belonging to the same customer.
                4. Transaction History - View all past transactions on this account.
                5. Account Details - View full account information including sort code and account type.
                6. Back - Return to the customer menu.

                Type 'help' or '?' at any time for assistance.
                """;
    }
}