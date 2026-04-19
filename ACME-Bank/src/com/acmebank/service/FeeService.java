package com.acmebank.service;

import com.acmebank.model.Account;
import com.acmebank.model.BusinessAccount;
import com.acmebank.model.Customer;
import com.acmebank.model.Transaction;
import com.acmebank.model.enums.TransactionType;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class FeeService {

    private static final double ANNUAL_FEE = BusinessAccount.ANNUAL_FEE;

    public boolean applyAnnualFee(BusinessAccount businessAccount) {

        if (businessAccount.getFeeDate() != null && businessAccount.getFeeDate().plusYears(1)
                .isAfter(LocalDate.now())) {
            System.out.println("Annual fee has already been applied this year for account: "
                    + businessAccount.getAccountNumber()
                    + "." + " Next due: " + businessAccount.getFeeDate().plusYears(1));
            return false;
        }

        if (businessAccount.getBalance() < ANNUAL_FEE && !businessAccount.isOverDraftAvailable()) {
            System.out.println("Warning! Insufficient balance to apply annual fee of £"
                    + String.format("%.2f", ANNUAL_FEE)
                    + " to account: " + businessAccount.getAccountNumber() + ". Fee not applied.");
            return false;
        }

        businessAccount.applyFee(ANNUAL_FEE);
        businessAccount.setFeeDate(LocalDate.now());

        Transaction feeTransaction = new Transaction(
                LocalDateTime.now(),
                TransactionType.FEE,
                "Annual business account fee",
                ANNUAL_FEE,
                businessAccount.getBalance()

        );

        System.out.println("Annual fee of £" + String.format("%.2f", ANNUAL_FEE) +
                " applied to account: " + businessAccount.getAccountNumber().getValue() +
                ". New balance: £" + String.format("%.2f", businessAccount.getBalance()));

        return true;
    }

}

