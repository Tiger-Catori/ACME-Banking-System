package com.acmebank.service;

import com.acmebank.infrastructure.logging.AuditLogger;
import com.acmebank.infrastructure.persistance.DataPersistance;
import com.acmebank.model.Account;
import com.acmebank.model.Customer;
import com.acmebank.model.Transaction;
import com.acmebank.model.enums.TransactionType;

import java.time.LocalDateTime;
import java.util.List;

public class TransactionService {
    AuditLogger auditLogger;
    DataPersistance dataPersistance;
    List<Customer> allCustomers;

    TransactionService(
            AuditLogger auditLogger,
            DataPersistance dataPersistance,
            List<Customer> allCustomers
    )   {
        this.auditLogger = auditLogger;
        this.dataPersistance = dataPersistance;
        this.allCustomers = allCustomers;
    }

    public void deposit(Account account, double amount) {
        account.deposit(amount);
        double newBalance = account.getBalance();
        // Creating transaction object
        Transaction transaction = Transaction.create(LocalDateTime.now(), TransactionType.DEPOSIT,"Cash Deposit", amount, newBalance);
        account.addTransaction(transaction);
        // Logging event
        auditLogger.log("Deposited £" + amount + " into account " + account.getAccountNumber());
        // Saving all customers.
        dataPersistance.saveCustomers(allCustomers);

    }

    public void withdraw(Account account, double amount) {
        account.withdraw(amount);
        double newBalance = account.getBalance();
        // Creating transaction object
        Transaction transaction = Transaction.create(LocalDateTime.now(), TransactionType.WITHDRAWAL, "Cash Withdrawal", amount, newBalance);
        account.addTransaction(transaction);
        // Logging event
        auditLogger.log("Withdrew £" + amount + " into account " + account.getAccountNumber());
        // Saving all customers
        dataPersistance.saveCustomers(allCustomers);
    }

    public void transfer (Account fromAccount, Account toAccount, double amount) {

    }
}
