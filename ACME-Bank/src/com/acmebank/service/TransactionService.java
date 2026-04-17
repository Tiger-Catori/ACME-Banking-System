package com.acmebank.service;

import com.acmebank.exceptions.InvalidAmountException;
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

    public static TransactionService create(AuditLogger logger,
                                            DataPersistance dataPersistance,
                                            List<Customer> customers) {
        return new TransactionService(logger, dataPersistance, customers);
    }

    public void deposit(Account account, double amount) {
        // Validating amount
        if (amount <= 0) {
            throw new InvalidAmountException("Deposit amount must be positive");
        }
        account.deposit(amount);
        double newBalance = account.getBalance();
        // Creating transaction object
        Transaction transaction = Transaction.create(
                LocalDateTime.now(),
                TransactionType.DEPOSIT,
                "Cash Deposit",
                amount, newBalance);
        account.addTransaction(transaction);
        // Logging event
        auditLogger.log("Deposited £" + amount + " into account " + account.getAccountNumber());
        // Saving all customers.
        dataPersistance.saveCustomers(allCustomers);

    }

    public void withdraw(Account account, double amount) {
        // Validating amount
        if (amount <= 0) {
            throw new InvalidAmountException("Withdrawal amount must be positive");
        }
        account.withdraw(amount);
        double newBalance = account.getBalance();
        // Creating transaction object
        Transaction transaction = Transaction.create(
                LocalDateTime.now(),
                TransactionType.WITHDRAWAL,
                "Cash Withdrawal",
                amount, newBalance);
        account.addTransaction(transaction);
        // Logging event
        auditLogger.log("Withdrew £" + amount + " into account " + account.getAccountNumber());
        // Saving all customers
        dataPersistance.saveCustomers(allCustomers);

    }

    public void transfer(Account fromAccount, Account toAccount, double amount) {
        if (amount <= 0) {
            throw new InvalidAmountException("Transfer amount must be positive");
        }
        if (fromAccount.getAccountNumber().equals(toAccount.getAccountNumber())) {
            throw new IllegalArgumentException("Cannot transfer to the same account");
        }

        fromAccount.withdraw(amount);
        toAccount.deposit(amount);

        Transaction sourceTransaction = Transaction.create(
                LocalDateTime.now(),
                TransactionType.TRANSFER,
                "Transfer to account " + toAccount.getAccountNumber(),
                amount, fromAccount.getBalance()
        );
        Transaction destinationTransaction = Transaction.create(
                LocalDateTime.now(),
                TransactionType.TRANSFER,
                "Transfer from account " + fromAccount.getAccountNumber(),
                amount, toAccount.getBalance()
        );

        fromAccount.addTransaction(sourceTransaction);
        toAccount.addTransaction(destinationTransaction);

        auditLogger.log("Transferred £" + amount + " from account " +
                fromAccount.getAccountNumber() + " to account " +
                toAccount.getAccountNumber());
        dataPersistance.saveCustomers(allCustomers);
    }

}
