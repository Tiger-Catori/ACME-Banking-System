package com.acmebank.model;

import java.util.ArrayList;
import java.util.List;

public class Customer {
    private final int customerID;
    private String firstName;
    private String lastName;
    private String address;
    List<Account> accountList = new ArrayList<>();

    public Customer(String firstName, String lastName) {
        // Customer ID is random 6 digit number
        this.customerID = (int) (Math.random() * 900_000) + 100_000;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public static Customer create(String firstName, String lastName) {
        return new Customer(firstName, lastName);
    }

    public void addAccount(Account account) {
        Class<?> accountType = account.getClass();

        boolean exists = accountList.stream()
                .anyMatch(obj -> obj.getClass() == accountType);

        if ((account instanceof IsaAccount || account instanceof BusinessAccount) && exists) {
            System.out.println("You already have a " + accountType.getSimpleName());
        }

        accountList.add(account);
    }

}
