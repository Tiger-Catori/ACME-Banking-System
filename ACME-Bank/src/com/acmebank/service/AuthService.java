package com.acmebank.service;

import com.acmebank.exceptions.CustomerNotFoundException;
import com.acmebank.model.Customer;

import java.util.List;

public class AuthService {
    private final List<Customer> customers;
    private Customer authenticatedCustomer = null;

    public AuthService(List<Customer> customers) {
        this.customers = customers;
    }

    public Customer authenticate(int customerID) throws CustomerNotFoundException {
        for (Customer customer : customers) {
            if (customer.getCustomerID() == customerID) {
                authenticatedCustomer = customer;
                System.out.println("Customer authenticated: " + customer.getFirstName() + " "
                        + customer.getLastName());
                return customer;
            }
        }
        throw new CustomerNotFoundException("No customer found with ID: " + customerID);
    }

    public Customer getAuthenticatedCustomer() {
        return authenticatedCustomer;
    }

    public boolean isAuthenticated() {
        return authenticatedCustomer != null;
    }

    public void logout() {
        System.out.println("Customer " + authenticatedCustomer.getFirstName()
                + " " + authenticatedCustomer.getLastName() + " logged out.");
        authenticatedCustomer = null;
    }
}
