package com.acmebank;

import com.acmebank.model.Account;
import com.acmebank.model.IsaAccount;
import com.acmebank.model.PersonalAccount;

public class Main {

    public static void main(String[] args) {
        Account account1 = new PersonalAccount(10.348);
        Account account2 = new PersonalAccount(89.132);
        Account account3 = new IsaAccount(101);
        account1.displayDetails();
        System.out.println();
        account3.displayDetails();
        System.out.println(account3.calculateInterest());

    }

}
