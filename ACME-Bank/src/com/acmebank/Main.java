package com.acmebank;

import com.acmebank.model.*;

public class Main {

    public static void main(String[] args) {
//        BusinessAccount account1 = BusinessAccount.create(33.546);
//        account1.displayDetails();

        Customer customer1 = Customer.create("Vanit", "Kashab");
        customer1.addAccount(BusinessAccount.create(24.987));
        customer1.addAccount(PersonalAccount.create(144.631));
        customer1.addAccount(BusinessAccount.create(900.232));


    }

}
