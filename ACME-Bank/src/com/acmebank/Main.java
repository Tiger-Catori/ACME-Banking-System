package com.acmebank;

import com.acmebank.exceptions.InvalidBusinessTypeException;
import com.acmebank.infrastructure.logging.AuditLogger;
import com.acmebank.infrastructure.logging.FileAuditLogger;
import com.acmebank.infrastructure.persistance.DataPersistance;
import com.acmebank.infrastructure.persistance.JsonPersistance;
import com.acmebank.model.*;
import com.acmebank.model.enums.BusinessType;
import com.acmebank.service.BusinessAccountValidator;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class Main {

    public static void main(String[] args) {
        // Create the logger (Log file in data/audit.log)
        AuditLogger logger = new FileAuditLogger("data/audit.log");
        logger.log("Banking system starting...");

        // 2. Setting up persistance (JSON file in data/customers.json)
        DataPersistance persistance = new JsonPersistance("data/customer.json", logger);

        // 3. Load existing customers (if any)
        List<Customer> customers = persistance.loadCustomers();
        logger.log("Loaded " + customers.size() + " existing customers.");

        // 4. If no data exists, create sample customers & accounts
        if (customers.isEmpty()) {
            logger.log("No existing data found. Creating sample customers...");


            // Customer 1: John Doe with a Personal Account
            Customer john = Customer.create("John", "Doe");
            PersonalAccount personalAcccount = PersonalAccount.create(2000.367);
            if (personalAcccount != null) {
                john.addAccount(personalAcccount);
                logger.log("Created Personal Account for John Doe: " + personalAcccount.getAccountNumber());

            } else {
                logger.logWarning("Failed to create Personal Account for John Doe (balance below £1.)");
            }

            // Customer 2: Jane Smith with an ISA Account
            Customer trevor = Customer.create("Trevor", "Smith");
            IsaAccount isaAccount = IsaAccount.create(5060.345);
            trevor.addAccount(isaAccount);
            logger.log("Created ISA Account for Jane Smith: " + isaAccount.getAccountNumber());

            // Customer 3: ACME Corp with a Business Account.
            Customer acme = Customer.create("ACME", "Corp");
            BusinessType eligibleType = BusinessType.SOLE_TRADER;

            BusinessAccount businessAccount = BusinessAccount.create(2750.199, eligibleType);
            if (businessAccount != null) {
                acme.addAccount(businessAccount);
                // Applying annual fee. (£120)
                businessAccount.applyYearlyFee();
                logger.log("Created Business Account for Acme Corp: " + businessAccount.getAccountNumber());
            } else {
                logger.logWarning("Failed to create Business Account for Acme Corp (ineligible business type)");
            }
            // Add all customer to the list
            customers.add(john);
            customers.add(trevor);
            customers.add(acme);
        }

        // 5. Save all customer to the JSON file
        persistance.saveCustomers(customers);
        logger.log("Saved " + customers.size() + " customer to data/customers.json");

        logger.log("Banking system finished.");
    }

}
