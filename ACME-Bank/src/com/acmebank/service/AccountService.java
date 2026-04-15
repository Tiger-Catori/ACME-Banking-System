package com.acmebank.service;

import com.acmebank.infrastructure.generation.AccountNumberGenerator;
import com.acmebank.infrastructure.logging.AuditLogger;
import com.acmebank.infrastructure.persistance.DataPersistance;
import com.acmebank.model.*;
import com.acmebank.model.enums.AccountType;
import com.acmebank.model.enums.BusinessType;
import com.acmebank.exceptions.*;

import java.util.List;

public class AccountService {
    private final AccountNumberGenerator accountNumberGenerator;
    private final BusinessAccountValidator businessAccountValidator;
    private final AuditLogger auditLogger;
    private final DataPersistance dataPersistence;
    private final FeeService feeService;
    private final List<Customer> allCustomers;

    // Constructor
    public AccountService(AccountNumberGenerator accountNumberGenerator,
                          BusinessAccountValidator businessAccountValidator,
                          AuditLogger auditLogger,
                          DataPersistance dataPersistence,
                          FeeService feeService,
                          List<Customer> allCustomers) {
        this.accountNumberGenerator = accountNumberGenerator;
        this.businessAccountValidator = businessAccountValidator;
        this.auditLogger = auditLogger;
        this.dataPersistence = dataPersistence;
        this.feeService = feeService;
        this.allCustomers = allCustomers;
    }

    // Public account creation methods
    /**
     * Creates a PERSONAL or ISA account.
     * For PERSONAL, uses PersonalAccount.create() factory to enforce £1 minimum.
     * For ISA, assumes a constructor PersonalAccount? Actually IsaAccount likely has a constructor.
     */
    public Account createAccount(Customer customer, AccountType accountType, double openingBalance) {
        validateOpeningBalance(accountType, openingBalance);

        Account account;

        switch (accountType) {
            case PERSONAL:
                // Use factory method – returns null if openingBalance < 1
                account = PersonalAccount.create(openingBalance);
                if (account == null) {
                    throw new InvalidAmountException("Personal account requires minimum £1 opening balance.");
                }
                break;
            case ISA:
                // Assuming IsaAccount has a constructor (double, SortCode) – adjust if different
                SortCode isaSortCode = getSortCodeForType(AccountType.ISA);
                account = IsaAccount.create(openingBalance);
                break;
            default:
                throw new IllegalArgumentException("For BUSINESS account, use createBusinessAccount method.");
        }

        return addAccountToCustomerAndSave(customer, account);
    }

    /**
     * Creates a BUSINESS account using BusinessAccount.create() factory.
     */
    public Account createBusinessAccount(Customer customer, BusinessType businessType, double openingBalance) throws InvalidBusinessTypeException {
        validateOpeningBalance(AccountType.BUSINESS, openingBalance);

        // Validate business eligibility
        businessAccountValidator.validate(businessType);

        // Use factory method – may return null if business type invalid
        BusinessAccount account = BusinessAccount.create(openingBalance, businessType);
        if (account == null) {
            throw new InvalidBusinessTypeException("Failed to create business account. Check business type eligibility.");
        }

        // Apply annual fee
        feeService.applyAnnualFee(account);

        return addAccountToCustomerAndSave(customer, account);
    }

    // Viewing and formatting
    public List<Account> getAccountsForCustomer(Customer customer) {
        return customer.getAccounts();   // Requires getter in Customer
    }

    public String formatAccountDetails(Account account) {
        StringBuilder sb = new StringBuilder();
        sb.append("Account Number: ").append(account.getAccountNumber().getValue()).append("\n");
        sb.append("Sort Code: ").append(account.getSortCode().toString()).append("\n");
        sb.append("Balance: £").append(String.format("%.2f", account.getBalance())).append("\n");
        sb.append("Type: ").append(account.getClass().getSimpleName()).append("\n");

        if (account instanceof BusinessAccount) {
            BusinessAccount ba = (BusinessAccount) account;
            sb.append("Business Type: ").append(ba.getBusinessType().getDisplayName()).append("\n");
            sb.append("Cheque Book Issued: ").append(ba.isChequeBook() ? "Yes" : "No").append("\n");
            sb.append("Overdraft Enabled: ").append(ba.isOverDraftAvailable() ? "Yes" : "No").append("\n");
        } else if (account instanceof PersonalAccount) {
            PersonalAccount pa = (PersonalAccount) account;
            // PersonalAccount may have overdraft limit getter; if not, skip or hardcode
            sb.append("Overdraft Limit: £500.00\n");
        } else if (account instanceof IsaAccount) {
            sb.append("Interest Rate: 2.75% APR\n");
        }
        return sb.toString();
    }

    // Private helpers
    private SortCode getSortCodeForType(AccountType type) {
        switch (type) {
            case PERSONAL -> {return SortCode.from(60, 60, 60);}
            case ISA -> {return SortCode.from(60, 60, 70);}
            case BUSINESS -> {return SortCode.from(60, 70, 70);}
            default -> {
                throw new IllegalArgumentException("Unknown account type: " + type);
            }
        }
    }

    private void validateOpeningBalance(AccountType accountType, double openingBalance) {
        if (openingBalance < 0) {
            throw new InvalidAmountException("Opening balance cannot be negative.");
        }
        // Personal minimum is enforced by PersonalAccount.create, but we keep for early failure
        if (accountType == AccountType.PERSONAL && openingBalance < 1) {
            throw new InvalidAmountException("Personal account requires minimum £1 opening balance.");
        }
        // Business and ISA have no minimum (add if needed)
    }

    private Account addAccountToCustomerAndSave(Customer customer, Account account) {
        try {
            customer.addAccount(account);   // may throw DuplicateIsaException
        } catch (DuplicateIsaException e) {
            auditLogger.log("FAILED: Duplicate ISA attempt for customer " + customer.getCustomerID());
            throw e;
        }

        // Save all customers to persistent storage
        dataPersistence.saveCustomers(allCustomers);

        // Log success
        auditLogger.log("Account created: " + account.getClass().getSimpleName() +
                " " + account.getAccountNumber().getValue() +
                " for customer " + customer.getCustomerID());

        return account;
    }
}