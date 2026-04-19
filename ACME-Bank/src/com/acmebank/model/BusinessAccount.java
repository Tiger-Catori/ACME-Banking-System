package com.acmebank.model;
import com.acmebank.exceptions.InvalidBusinessTypeException;
import com.acmebank.model.enums.BusinessType;
import com.acmebank.service.BusinessAccountValidator;
import com.acmebank.exceptions.InsufficientFundsException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

public class BusinessAccount extends Account {
    public static final double ANNUAL_FEE = 120.00;

    private final BusinessType businessType;

    private boolean overDraftAvailable = false;
    private boolean chequeBook = false;
    private boolean businessLoanActive = false;
    private boolean feePaid = false;
    private LocalDate feeDate = null;

    private double overDraft = 0.00;

    private BusinessAccount(double currentBalance, BusinessType businessType) {
        super(currentBalance, SortCode.from(60,70,70));
        this.businessType = businessType;
    }

    public static BusinessAccount create(double startingBalance, BusinessType businessType) {
        BusinessAccountValidator validator = new BusinessAccountValidator();
        try {
            validator.validate(businessType);
            return new BusinessAccount(startingBalance, businessType);
        } catch (InvalidBusinessTypeException | IllegalArgumentException e) {
            System.out.println("Could not create business account: " + e.getMessage());
            return null;
        }
    }

    @Override
    public double deposit(double amount) {
        if (amount >= 0) {
            setBalance(getBalance() + amount);
        }
        return getBalance();
    }

    @Override
    public double withdraw(double amount) throws InsufficientFundsException {
        if (amount <= 0) {
            System.out.println("Could not withdraw negative amount");
            return getBalance();
        }

        double limit = overDraftAvailable ? overDraft : 0.00;

        if (getBalance() - amount >= - limit) {
            setBalance(getBalance() - amount);

            if (getBalance() < 0) {
                System.out.println("Warning: account is now in overdraft. " +
                        "Current balance: £" + String.format("%.2f", getBalance()));
            }
        } else {
            throw new InsufficientFundsException(
                    "Insufficient funds. Balance: £" + String.format("%.2f", getBalance()) +
                            ", Attempted: £" + String.format("%.2f", amount)
            );
        }
        return getBalance();
    }

    @Override
    public double calculateInterest() {
        return 0;
    }

    public void applyYearlyFee() {
        setBalance( new BigDecimal(getBalance() - ANNUAL_FEE)
                .setScale(2, RoundingMode.HALF_UP).doubleValue());
    }


    public void issueChequeBook() {
        if (chequeBook) {
                System.out.println("A cheque book has already been issued for this account.");
        } else {
            chequeBook = true;
            System.out.println("A cheque book has been issued for this account. " + getAccountNumber());
        }
    }

    public void activateOverdraft(double limit) {
        this.overDraft = limit;
        this.overDraftAvailable = true;
    }

    public void deactivateOverdraft(double limit) {
        this.overDraft = 0.00;
        this.overDraftAvailable = false;
    }

    private double loanAmount = 0.00;

    public void loanTaken(double amount) {
        if (businessLoanActive) {
            System.out.println("Loan has already been taken for this account.");
        } else {
            this.loanAmount = amount;
            businessLoanActive = true;
            System.out.println("Loan has been taken for this account. " +
                    "Loan of £ " + String .format("%.2f", amount) + getAccountNumber());
        }

    }

    public void loanRepayment(double amount) {
        loanAmount -= amount;

        if (amount <= 0) {
            System.out.println("Repayment must be greater than 0.");
            return;
        }

        if(amount > loanAmount) {
            System.out.println("Repayment of £ " + String.format("%.2f", amount) +
                    "exceeds the remaining loan balance of £ " +  String.format("%.2f", loanAmount));
            return;
        }

        if(loanAmount <= 0.00) {
            loanAmount = 0.00;
            businessLoanActive = false;
            System.out.println("Loan is fully paid off.");
        } else {
            System.out.println("Payment Accepted. Remaining loan balance: £" + String.format("%.2f", loanAmount));
        }

    }

    public void applyFee(double amount) {
        setBalance(new BigDecimal(getBalance() - amount)
                .setScale(2, RoundingMode.HALF_UP).doubleValue());
    }

    public void setFeeDate(LocalDate date) {
        this.feeDate = date;
    }


    @Override
    public void displayDetails() {
        super.displayDetails();
        System.out.println(
                "Account Type: Business Account" +
                        "\nBusiness Type: " + businessType.getDisplayName() +
                        "\nAnnual Fee: £" + String.format("%.2f", ANNUAL_FEE) +
                        "\nCheque Book Issued: " + (chequeBook ? "Yes" : "No") +
                        "\nOverdraft Active: " + (overDraftAvailable ? "Yes — Limit: £"
                        + String.format("%.2f", overDraft) : "No") +
                        "\nLoan Active: " + (businessLoanActive ? "Yes - Amount: £ " + String.format("%.2f", loanAmount)
                        : "No")

        );
    }

    public BusinessType getBusinessType() {
        return businessType;
    }

    public double getOverDraft() {
        return overDraft;
    }

    public boolean isOverDraftAvailable() {
        return overDraftAvailable;
    }

    public boolean isChequeBook() {
        return chequeBook;
    }

    public double getAnnualFee() {
        return ANNUAL_FEE;
    }

    public boolean isBusinessLoanActive() {
        return businessLoanActive;
    }

    public LocalDate getFeeDate() {
        return feeDate;
    }


}
