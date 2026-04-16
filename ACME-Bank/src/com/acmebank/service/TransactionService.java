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

    public void deposit(Account account, double amount) {
        // Validating amount
        if (amount <= 0) {
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
        } else {
            throw new InvalidAmountException("Deposit amount must be positive");
        }

    }

    public void withdraw(Account account, double amount) {
        // Validating amount
        if (amount > 0) {
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
        } else {
            throw new InvalidAmountException("Withdrawal amount must be positive");
        }

    }

    public void transfer(Account fromAccount, Account toAccount, double amount) {
        // Validation
        if (amount > 0 && (fromAccount.getAccountNumber() != toAccount.getAccountNumber())) {
            fromAccount.withdraw(amount);
            toAccount.deposit(amount);
            // Creating transaction for source account
            Transaction sourceTransaction = Transaction.create(
                    LocalDateTime.now(),
                    TransactionType.TRANSFER,
                    "Transfer to account " + toAccount.getAccountNumber(),
                    amount, fromAccount.getBalance()
            );
            // Creating transaction for destination account
            Transaction destinationTransaction = Transaction.create(
                    LocalDateTime.now(),
                    TransactionType.TRANSFER,
                    "Transfer from account " + fromAccount.getAccountNumber(),
                    amount, toAccount.getBalance()
            );
            // Adding transactions
            toAccount.addTransaction(destinationTransaction);
            fromAccount.addTransaction(sourceTransaction);
            // Logging it
            auditLogger.log("Transferred £" + amount + " from account " +
                    fromAccount.getAccountNumber() + " to account " +
                    toAccount.getAccountNumber());
            // Saving customers
            dataPersistance.saveCustomers(allCustomers);
        }

    }
}
