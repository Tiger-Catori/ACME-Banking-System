package com.acmebank;

import com.acmebank.exceptions.InvalidBusinessTypeException;
import com.acmebank.model.*;
import com.acmebank.model.enums.BusinessType;
import com.acmebank.service.BusinessAccountValidator;

import java.util.Objects;

public class Main {

    public static void main(String[] args) {
        BusinessAccount account = BusinessAccount.create(233.239, BusinessType.PARTNERSHIP);
        account.displayDetails();
        System.out.println();
        IsaAccount account2 = IsaAccount.create(447.356);
        IsaAccount account3 = IsaAccount.create(99.234);
        account2.displayDetails();
        System.out.println(account2.calculateInterest());

        Customer customer1 = Customer.create("Peter", "Parker");
        customer1.addAccount(account2);
        customer1.addAccount(account3);




    }

}
