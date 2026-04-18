package com.acmebank.cli.handlers;

import com.acmebank.model.*;
import com.acmebank.model.enums.TransactionType;

import java.util.List;

public class DisplayFormatter {

    // Format a single account summary (one line)
    public static String formatAccountSummary(Account account) {
        String type = account.getClass().getSimpleName();
        // Remove account suffix if present
        if (type.endsWith("Account")) {
            type = type.substring(0, type.length() - 7);
        }
        return String.format("Account #%s (%s): £%.2f",
                account.getAccountNumber().getValue(),
                type, account.getBalance());
    }

    // Formatting details account info
    public static String formattedAccountDetails(Account account) {
        StringBuilder sb = new StringBuilder();
        sb.append("---------------------------------------------\n");
        sb.append("Account Number: ").append(account.getAccountNumber().getValue()).append("\n");
        sb.append("Sort Code:      ").append(account.getSortCode().toString()).append("\n");
        sb.append("Balance:        £").append(formatBalance(account.getBalance())).append("\n");
        sb.append("Type:           ").append(account.getClass().getSimpleName()).append("\n");

        if (account instanceof PersonalAccount) {
            PersonalAccount pa = (PersonalAccount) account;

            // Overdraft limit is hardcoded to £100 in PersonalAccount
            sb.append("Overdraft Limit: £100.00\n");
        } else if (account instanceof IsaAccount) {
            sb.append("Interest Rate: 2.75% APR \n");
        } else if (account instanceof BusinessAccount) {
            BusinessAccount ba = (BusinessAccount) account;
            sb.append("Business Type:  ").append(ba.getBusinessType().getDisplayName()).append("\n");
            sb.append("Cheque Book:     ").append(ba.isChequeBook() ? "Issued" : "Not issued").append("\n");
            sb.append("Overdraft:       ").append(ba.isOverDraftAvailable() ? "Enabled" : "Disabled").append("\n");
            if (ba.isBusinessLoanActive()) {
                sb.append("Loan Active: Yes\n");
            }
        }
        sb.append("---------------------------------------------\n");
        return sb.toString();
    }

    // Format customer information
    public static String formatCustomerInfo(Customer customer) {
        return String.format("Customer: %s %s (ID %d)",
                customer.getFirstName(),
                customer.getLastName(),
                customer.getCustomerID());
    }

    // Formatting a single transaction to display
    public static String formatTransaction(Transaction transaction) {
        String sign = "";
        if (transaction.getType() == TransactionType.WITHDRAWAL ||
        transaction.getType() == TransactionType.FEE) {
            sign = "-";
        }
        return String.format("[%s] %s: %s£%.2f → Balance: £%.2f",
                transaction.getTimestamp().toLocalDate(),
                transaction.getType().getDisplayName(),
                sign, transaction.getAmount(),
                transaction.getBalanceAfter());
    }

    // Format transaction history (list)
    public static String formatTransactionHistory(List<Transaction>transactions) {
        if (transactions.isEmpty()) {
            return "No transaction found.";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < transactions.size(); i++) {
            sb.append(i + 1).append(". ");
            sb.append(formatTransaction(transactions.get(i)));
            sb.append("\n");
        }
        return sb.toString();
    }

    // Format a balance with £ sign and 2 dp.
    public static String formatBalance(double balance) {
        return String.format("%.2f", balance);
    }

    // Format a list of accounts for a customer (summary view)
    public static String formatAccountList(List<Account> accounts) {
        if (accounts.isEmpty()) {
            return "No accounts found.";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < accounts.size(); i++) {
            sb.append(i + 1).append(". ");
            sb.append(formatAccountSummary(accounts.get(i)));
            sb.append("\n");
        }
        return sb.toString();
    }
}
