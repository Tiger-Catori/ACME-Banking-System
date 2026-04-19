package com.acmebank.cli.menus;
import com.acmebank.cli.MenuController;
import com.acmebank.cli.handlers.InputHandler;
import com.acmebank.exceptions.CustomerNotFoundException;
import com.acmebank.model.Customer;

public class MainMenu extends Menu {

    public MainMenu(InputHandler inputHandler) {
        super(inputHandler);
    }

    @Override
    public void display(MenuController controller) {
        printTitle("Acme Bank — Teller System");

        if (controller.getStartupMessage() != null) {
            System.out.println(controller.getStartupMessage());
        }

        System.out.println("1. Authenticate Customer");
        System.out.println("2. Exit");
        System.out.println("==========================================");
        System.out.println("Type 'help' or '?' for assistance.");
        System.out.println("==========================================");
    }

    @Override
    public void handleInput(MenuController menuController, String choice) {
        switch (choice) {
            case "1": authenticateCustomer(menuController);
            break;
            case "2": exitSystem(menuController);
            break;
            default: printInvalidOptions();
        }
    }

    private void authenticateCustomer(MenuController menuController) {
        int customerID = inputHandler.readInt("Enter Customer ID:");

        try {
            Customer customer = menuController.getAuthService().authenticate(customerID);
            menuController.setCurrentCustomer(customer);

            System.out.println("\nAuthentication successful. Welcome, "
                    + customer.getFirstName() + " " + customer.getLastName() + ".");

            // Move straight into the customer menu
            menuController.switchToMenu(new CustomerMenu(inputHandler));

        } catch (CustomerNotFoundException e) {
            System.out.println("Customer not found. Please try again.");
        }
    }

    private void exitSystem(MenuController menuController) {
        System.out.println("Shutting down Acme Bank Teller System. Bye!");
        menuController.stop();
    }

    @Override
    public String getHelpText() {
        return """
                === Main Menu Help ===
                Option 1 — Authenticate Customer:
                  Enter the customer's unique 6-digit Customer ID.
                  All account operations require authentication first.
                
                Option 2 — Exit:
                  Closes the teller system safely.
                
                Type 'help' or '?' at any menu to see relevant help.
                """;
    }
}
