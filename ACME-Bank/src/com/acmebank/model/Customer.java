package com.acmebank.model;

import java.util.ArrayList;
import java.util.List;

public class Customer {
    private int customerID;
    private String firstName;
    private String lastName;

    public Customer(int customerID, String firstName, String lastName) {
        this.customerID = customerID;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    List<Account> accountList = new ArrayList<>();
//    accountList.add(new PersonalAccount());
//    accountList.add(new BusinessAccount());
//    accountList.add(new IsaAccount());

//    public void addNewAccount() {
//        if (accountList.contains(typeof(IsaAccount)))
//    }
}
