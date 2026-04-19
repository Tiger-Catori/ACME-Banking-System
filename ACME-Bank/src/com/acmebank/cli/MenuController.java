package com.acmebank.cli;
import com.acmebank.cli.handlers.*;
import com.acmebank.cli.menus.*;
import com.acmebank.model.*;
import com.acmebank.service.*;

import java.util.List;

public class MenuController {

    private final AuthService authService;
    private final AccountService accountService;
    private final TransactionService transactionService;
    private final FeeService feeService;
    private final InterestCalculator interestCalculator;

    private final HelpSystem helpSystem;
    private final InputHandler inputHandler;

    private final List<Customer> customers;
    private Customer authenticatedCustomer = null;
    private boolean running = true;

    public MenuController(AuthService authService,
                          AccountService accountService,
                          TransactionService transactionService,
                          FeeService feeService,
                          InterestCalculator interestCalculator,
                          List<Customer> customers,
                          HelpSystem helpSystem,
                          InputHandler inputHandler) {

        this.authService = authService;
        this.accountService = accountService;
        this.transactionService = transactionService;
        this.feeService = feeService;
        this.interestCalculator = interestCalculator;
        this.customers = customers;
        this.helpSystem = helpSystem;
        this.inputHandler = inputHandler;
    }

    public void start() {
        System.out.println("====================");
        System.out.println("Welcome to ACME Bank");
        System.out.println("====================");

        while (running) {
            if (authenticatedCustomer == null) {
                MainMenu mainMenu = new MainMenu(helpSystem, inputHandler);
                    mainMenu.run(this);
            } // add customer menu here when created
        }
        System.out.print("AMCE Bank Teller System closed. Bye");

    }

    public void stop() {
        this.running = false;
    }

    public Customer getAuthenticatedCustomer() {
        return authenticatedCustomer;
    }

    public void logout() {
        if(authenticatedCustomer != null) {
            System.out.println("You have logged out: " + authenticatedCustomer.getFirstName() + " "
                    + authenticatedCustomer.getLastName()+ ".");
        }
        this.authenticatedCustomer = null;
    }

    public void setAuthenticatedCustomer(Customer authenticatedCustomer) {
        this.authenticatedCustomer = authenticatedCustomer;
    }

    public AuthService getAuthService() {
        return authService;
    }
    public AccountService getAccountService() {
        return accountService;
    }
    public TransactionService getTransactionService() {
        return transactionService;
    }
    public FeeService getFeeService() {
        return feeService;
    }
    public InterestCalculator getInterestCalculator() {
        return interestCalculator;
    }
    public HelpSystem getHelpSystem() {
        return helpSystem;
    }
    public InputHandler getInputHandler() {
        return inputHandler;
    }
    public List<Customer> getCustomers() {
        return customers;
    }


}
