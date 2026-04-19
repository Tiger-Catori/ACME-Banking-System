package com.acmebank.cli.menus;

import com.acmebank.cli.handlers.HelpSystem;
import com.acmebank.cli.handlers.InputHandler;
import com.acmebank.cli.MenuController;

public abstract class Menu {

    protected final InputHandler inputHandler;
    protected final HelpSystem helpSystem;

    public Menu(InputHandler inputHandler, HelpSystem helpSystem) {
        this.inputHandler = inputHandler;
        this.helpSystem = helpSystem;
    }

    public abstract void display();
    public abstract String getHelpText();
    public abstract void handleInput(MenuController menuController, String choice);

    public void run(MenuController menuController) {
        boolean running = true;

        while (running) {
            display();
            String choice = inputHandler.readLine("Enter your choice:");

            if(choice.equalsIgnoreCase("exit")) {
                System.out.println("Exiting to previous menu...");
                running = false;
            } else if(choice.equalsIgnoreCase("help") || choice.equals("?")) {
                helpSystem.showHelp(this);
            } else {
                handleInput(menuController, choice);
            }
        }
    }

    protected void printTitle (String title) {
        System.out.println("\n==========================================");
        System.out.println(title);
        System.out.println("==========================================");
    }

    protected void printInvalidOptions() {
        System.out.println("Please select a valid option!");
    }


}
