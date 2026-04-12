package com.acmebank;

import com.acmebank.exceptions.InvalidBusinessTypeException;
import com.acmebank.infrastructure.logging.AuditLogger;
import com.acmebank.infrastructure.logging.FileAuditLogger;
import com.acmebank.model.*;
import com.acmebank.model.enums.BusinessType;
import com.acmebank.service.BusinessAccountValidator;

import java.io.IOException;
import java.util.Objects;

public class Main {

    public static void main(String[] args) {

        AuditLogger logger = new FileAuditLogger("data/audit.log");
        logger.log("ACME system Launched");
        logger.logEvent("AUTHENTICATION", "Customer 001 authenticated");
        logger.logWarning("ISA withdrawal limit nearly reached for account 12345678");
        logger.logError("Failed to save customers", new IOException("Disk full"));

//        BusinessAccount account = BusinessAccount.create(233.239, BusinessType.PARTNERSHIP);
//        account.displayDetails();
//        System.out.println();
//        IsaAccount account2 = IsaAccount.create(447.356);
//        IsaAccount account3 = IsaAccount.create(99.234);
//        account2.displayDetails();
//        System.out.println(account2.calculateInterest());
//
//        Customer customer1 = Customer.create("Peter", "Parker");
//        customer1.addAccount(account2);
//        customer1.addAccount(account3);




    }

}
