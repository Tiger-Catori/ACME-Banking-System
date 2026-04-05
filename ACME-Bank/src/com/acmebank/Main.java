package com.acmebank;

import com.acmebank.model.Account;
import com.acmebank.model.PersonalAccount;

public class Main {

    public static void main(String[] args) {
        Account account1 = new PersonalAccount(10.348);
        Account account2 = new PersonalAccount(89.139);
        account1.displayDetails();
        System.out.println();
        account2.displayDetails();
    }

}
