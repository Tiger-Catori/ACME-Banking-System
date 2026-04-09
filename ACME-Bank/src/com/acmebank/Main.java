package com.acmebank;

import com.acmebank.exceptions.InvalidBusinessTypeException;
import com.acmebank.model.*;
import com.acmebank.model.enums.BusinessType;
import com.acmebank.service.BusinessAccountValidator;

import java.util.Objects;

public class Main {

    public static void main(String[] args) {
//        BusinessAccount account1 = BusinessAccount.create(33.546);
//        account1.displayDetails();

        Customer customer1 = Customer.create("Nathan", "Drake");
        BusinessAccount businessAccount = BusinessAccount.create(2000.00,BusinessType.SOLE_TRADER);
        businessAccount.activateOverdraft(5000.00);
        businessAccount.issueChequeBook();
        businessAccount.loanTaken(10000.00);
        customer1.addAccount(businessAccount);

        businessAccount.displayDetails();


        customer1.addAccount(IsaAccount.create(30.232));



    }

}
