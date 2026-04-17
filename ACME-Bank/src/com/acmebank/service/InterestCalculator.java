package com.acmebank.service;

import com.acmebank.model.Account;
import com.acmebank.model.IsaAccount;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class InterestCalculator {

    private final static double INTEREST_RATE = 0.0275;

    public BigDecimal calculateInterest(IsaAccount account) {
        BigDecimal balance = BigDecimal.valueOf(account.getBalance());
        BigDecimal rate = BigDecimal.valueOf(INTEREST_RATE);

        return balance.multiply(rate).setScale(2, RoundingMode.HALF_UP);
    }
}
