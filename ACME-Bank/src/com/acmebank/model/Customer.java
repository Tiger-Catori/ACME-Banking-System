package com.acmebank.model;

import com.acmebank.exceptions.DuplicateAccountException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.acmebank.exceptions.DuplicateAccountException;

import java.util.ArrayList;
import java.util.List;

public class Customer {
    private final int customerID;
    private String firstName;
    private String lastName;
    private String address;
    private List<Account> accountList = new ArrayList<>();

    @JsonCreator
    public Customer(@JsonProperty("firstname") String firstName,
                    @JsonProperty("lastname") String lastName) {
        // Customer ID is random 6 digit number
        this.customerID = (int) (Math.random() * 900_000) + 100_000;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public static Customer create(String firstName, String lastName) {
        return new Customer(firstName, lastName);
    }

    // Getters.
    public List<Account> getAccounts() {
        return new ArrayList<>(accountList);
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void addAccount(Account account) throws DuplicateAccountException {
        Class<?> accountType = account.getClass();

        boolean exists = accountList.stream()
                .anyMatch(obj -> obj.getClass() == accountType);

        if (account instanceof IsaAccount && exists || account instanceof BusinessAccount && exists) {
            throw new DuplicateAccountException(
                    account.getClass().getSimpleName() + " account already exists. Only one " + account.getClass().getSimpleName() + " is permitted per customer.");
        }
        accountList.add(account);
    }

}