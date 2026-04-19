package com.acmebank.cli;

import com.acmebank.cli.handlers.InputHandler;
import com.acmebank.cli.menus.MainMenu;
import com.acmebank.cli.menus.Menu;
import com.acmebank.model.Customer;
import com.acmebank.service.AuthService;
import com.acmebank.service.AccountService;
import com.acmebank.service.TransactionService;
import com.acmebank.infrastructure.logging.AuditLogger;
import com.acmebank.infrastructure.persistance.DataPersistance;

import java.util.List;

public class MenuController {
    private final AuthService authService;
    private final AccountService accountService;
    private final TransactionService transactionService;
    private final AuditLogger auditLogger;
    private final DataPersistance dataPersistence;
    private final List<Customer> allCustomers;
    private final InputHandler inputHandler;
    private String startupMessage;

    private Customer currentCustomer;
    private Menu currentMenu;
    private boolean running;
    private boolean menuChanged;

    public MenuController(AuthService authService,
                          AccountService accountService,
                          TransactionService transactionService,
                          AuditLogger auditLogger,
                          DataPersistance dataPersistence,
                          List<Customer> allCustomers,
                          InputHandler inputHandler) {
        this.authService = authService;
        this.accountService = accountService;
        this.transactionService = transactionService;
        this.auditLogger = auditLogger;
        this.dataPersistence = dataPersistence;
        this.allCustomers = allCustomers;
        this.inputHandler = inputHandler;
        this.currentCustomer = null;
        this.startupMessage = null;
        this.running = true;
        this.menuChanged = false;
    }

    public void start() {
        System.out.println("Welcome to ACME Bank Teller System");
        System.out.println("===================================");
        currentMenu = new MainMenu(inputHandler);

        while (running) {
            menuChanged = false;
            currentMenu.run(this);
            if (!running) break;
            // If menu changed, loop will run the new currentMenu
        }

        inputHandler.close();
        System.out.println("Goodbye!");
    }

    public void stop() {
        running = false;
    }

    public void switchToMenu(Menu newMenu) {
        this.currentMenu = newMenu;
        this.menuChanged = true;
    }

    public boolean hasMenuChanged() {
        return menuChanged;
    }

    // Getters
    public String getStartupMessage() {
        return startupMessage;
    }
    public void setStartupMessage(String startupMessage) {
        this.startupMessage = startupMessage;
    }
    public AuthService getAuthService() { return authService; }
    public AccountService getAccountService() { return accountService; }
    public TransactionService getTransactionService() { return transactionService; }
    public AuditLogger getAuditLogger() { return auditLogger; }
    public DataPersistance getDataPersistence() { return dataPersistence; }
    public List<Customer> getAllCustomers() { return allCustomers; }
    public InputHandler getInputHandler() { return inputHandler; }

    public Customer getCurrentCustomer() { return currentCustomer; }
    public void setCurrentCustomer(Customer customer) {
        this.currentCustomer = customer;
        if (customer != null) {
            auditLogger.log("Teller authenticated customer: " + customer.getCustomerID());
        } else {
            auditLogger.log("Teller logged out current customer");
        }
    }

    public void saveAllData() {
        dataPersistence.saveCustomers(allCustomers);
    }
}