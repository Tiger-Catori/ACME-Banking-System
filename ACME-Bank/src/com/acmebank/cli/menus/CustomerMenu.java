package com.acmebank.cli.menus;

import com.acmebank.cli.MenuController;
import com.acmebank.cli.handlers.DisplayFormatter;
import com.acmebank.cli.handlers.InputHandler;
import com.acmebank.cli.validators.ConsoleInputValidator;
import com.acmebank.exceptions.InvalidAmountException;
import com.acmebank.exceptions.InvalidBusinessTypeException;
import com.acmebank.model.Account;
import com.acmebank.model.Customer;
import com.acmebank.model.enums.AccountType;
import com.acmebank.model.enums.BusinessType;

public class CustomerMenu extends Menu {

    public CustomerMenu(InputHandler inputHandler) {
        super(inputHandler);
    }

    @Override
    public void display(MenuController controller) {
        printTitle("Customer Menu");
        Customer customer = controller.getCurrentCustomer();
        if (customer != null) {
            System.out.println("Logged in as: " + customer.getFirstName() + " " + customer.getLastName()
                    + " (ID " + customer.getCustomerID() + ")");
            System.out.println("------------------------------------------");
        }

        System.out.println("1. View All Accounts");
        System.out.println("2. Create New Account");
        System.out.println("3. Select Account");
        System.out.println("4. Logout");
        System.out.println("==========================================");
    }

    @Override
    public void handleInput(MenuController controller, String choice) {
        Customer customer = controller.getCurrentCustomer();
        if (customer == null) {
            System.out.println("No customer authenticated. Returning to main menu.");
            controller.switchToMenu(new MainMenu(inputHandler));
            return;
        }

        switch (choice) {
            case "1" -> viewAllAccounts(customer);
            case "2" -> createNewAccount(controller, customer);
            case "3" -> selectAccount(controller, customer);
            case "4" -> logout(controller);
            default -> printInvalidOptions();
        }
    }

    private void viewAllAccounts(Customer customer) {
        System.out.println("\n--- Your Accounts ---");
        System.out.println(DisplayFormatter.formatAccountList(customer.getAccounts()));
        inputHandler.readLine("Press Enter to continue...");
    }

    private void createNewAccount(MenuController controller, Customer customer) {
        System.out.println("\n--- Create New Account ---");
        System.out.println("Account types: PERSONAL, ISA, BUSINESS");
        String typeStr = inputHandler.readLine("Enter account type: ");
        AccountType type;
        try {
            type = ConsoleInputValidator.validateAccountType(typeStr);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
            return;
        }

        double openingBalance = inputHandler.readDouble("Enter opening balance: £");
        try {
            if (type == AccountType.BUSINESS) {
                System.out.println("Business types: SOLE_TRADER, PARTNERSHIP, LIMITED_COMPANY");
                String bizStr = inputHandler.readLine("Enter business type: ");
                BusinessType bizType = ConsoleInputValidator.validateBusinessType(bizStr);
                controller.getAccountService().createBusinessAccount(customer, bizType, openingBalance);
                System.out.println("Business account created successfully.");
            } else {
                controller.getAccountService().createAccount(customer, type, openingBalance);
                System.out.println("Account created successfully.");
            }
            controller.saveAllData();
        } catch (InvalidAmountException | InvalidBusinessTypeException | IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
        }
    }

    private void selectAccount(MenuController controller, Customer customer) {
        if (customer.getAccounts().isEmpty()) {
            System.out.println("No accounts available.");
            return;
        }
        System.out.println(DisplayFormatter.formatAccountList(customer.getAccounts()));
        String accNum = inputHandler.readLine("Enter account number: ");
        Account selected = null;
        for (Account acc : customer.getAccounts()) {
            if (acc.getAccountNumber().getValue().equals(accNum)) {
                selected = acc;
                break;
            }
        }
        if (selected == null) {
            System.out.println("Account not found.");
            return;
        }
        controller.switchToMenu(new AccountOperationsMenu(inputHandler, selected));
    }

    private void logout(MenuController controller) {
        controller.setCurrentCustomer(null);
        System.out.println("Logged out successfully.");
        controller.switchToMenu(new MainMenu(inputHandler));
    }

    @Override
    public String getHelpText() {
        return """
                === Customer Menu Help ===
                1. View All Accounts — Shows all accounts for the current customer.
                2. Create New Account — Opens a new personal, ISA, or business account.
                3. Select Account — Choose an account to deposit, withdraw, transfer, etc.
                4. Logout — End this customer session and return to main menu.
                """;
    }
}