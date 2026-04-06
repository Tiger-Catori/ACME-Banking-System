package com.acmebank;

import com.acmebank.model.Account;
import com.acmebank.model.IsaAccount;
import com.acmebank.model.PersonalAccount;

public class Main {

    public static void main(String[] args) {
        Account account1 = PersonalAccount.create(0.3);
        Account account2 = PersonalAccount.create(34.135);
        // account1.displayDetails();
        // System.out.println(account1.getAccountNumber());
        System.out.println();
        account2.displayDetails();

    }

}
