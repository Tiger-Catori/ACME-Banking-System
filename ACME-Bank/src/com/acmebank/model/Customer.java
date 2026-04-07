package com.acmebank.model;

import java.util.ArrayList;
import java.util.List;

public class Customer {
    private final int customerID;
    private String firstName;
    private String lastName;
    private String address;
    List<Account> accountList = new ArrayList<>();

    public Customer(int customerID, String firstName, String lastName) {
        this.customerID = customerID;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public void addAccount(Account account) {
        boolean hasIsaAccount = accountList.stream()
                .anyMatch(obj -> obj instanceof IsaAccount);

        boolean hasBusinessAccount = accountList.stream()
                .anyMatch(obj -> obj instanceof BusinessAccount);

        if (!hasIsaAccount) {
            accountList.add(account);
        } else if (!hasBusinessAccount) {
            accountList.add(account);
        } else {

        }
    }

    // List<Account> accountList = new ArrayList<>();
//    accountList.add(new PersonalAccount());
//    accountList.add(new BusinessAccount());
//    accountList.add(new IsaAccount());

//    public void addNewAccount() {
//        if (accountList.contains(typeof(IsaAccount)))
//    }
}
