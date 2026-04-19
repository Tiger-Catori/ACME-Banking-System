package com.acmebank.cli.menus;

import com.acmebank.cli.MenuController;
import com.acmebank.cli.handlers.InputHandler;
import com.acmebank.cli.handlers.HelpSystem;

public abstract class Menu {
    protected final InputHandler inputHandler;

    public Menu(InputHandler inputHandler) {
        this.inputHandler = inputHandler;
    }

    public abstract void display(MenuController controller);
    public abstract String getHelpText();
    public abstract void handleInput(MenuController controller, String choice);

    public void run(MenuController controller) {
        while (true) {
            display(controller);
            String choice = inputHandler.readLine("Enter your choice: ");

            if (choice.equalsIgnoreCase("help") || choice.equals("?")) {
                HelpSystem.showHelp(this);
                continue;
            }

            handleInput(controller, choice);

            if (controller.hasMenuChanged()) {
                break;
            }
        }
    }

    protected void printTitle(String title) {
        System.out.println("\n==========================================");
        System.out.println(title);
        System.out.println("==========================================");
    }

    protected void printInvalidOptions() {
        System.out.println("Please select a valid option!");
    }
}