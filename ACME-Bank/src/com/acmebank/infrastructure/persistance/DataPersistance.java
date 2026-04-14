package com.acmebank.infrastructure.persistance;

import com.acmebank.model.Customer;

import java.util.List;

public interface DataPersistance {
    // Defines the contract for any data storage mechanism.

    // writes all customer data to storage
    void saveCustomers(List<Customer> customerList);

    // reads all customer data from storage and returns a list.
    List<Customer> loadCustomers();

}
