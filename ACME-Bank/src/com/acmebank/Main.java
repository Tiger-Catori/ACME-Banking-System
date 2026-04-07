package com.acmebank;

import com.acmebank.model.Account;
import com.acmebank.model.BusinessAccount;
import com.acmebank.model.IsaAccount;
import com.acmebank.model.PersonalAccount;

public class Main {

    public static void main(String[] args) {
        Account account1 = PersonalAccount.create(22.469);
        Account account2 = PersonalAccount.create(446.7895);
        Account isaAccount1 = IsaAccount.create(45.566);
        Account businessAccount1 = BusinessAccount.create(33.218);
        // System.out.println();
//        account1.displayDetails();
//        System.out.println();
//        account2.displayDetails();
        isaAccount1.displayDetails();
        System.out.println();
        businessAccount1.displayDetails();

    }

}
