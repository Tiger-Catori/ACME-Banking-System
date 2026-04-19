package com.acmebank.cli.handlers;

import com.acmebank.cli.menus.Menu;

public class HelpSystem {

    public static void showHelp(Menu menu) {
        System.out.println("\n========== HELP ==========");
        System.out.println(menu.getHelpText());
        System.out.println("==========================\n");
    }

    public static void showAccountTypeHelp() {
        System.out.println("\n========== ACCOUNT TYPES ==========");
        System.out.println("Personal Account — Sort code: 60-60-60. Min opening balance £1. Overdraft up to £100.");
        System.out.println("ISA Account — Sort code: 60-60-70. One per customer. 2.75% APR interest.");
        System.out.println("Business Account — Sort code: 60-70-70. Eligible types: Sole Trader, Partnership, Limited Company. Annual fee £120.");
        System.out.println("===================================\n");
    }

    public static void showTransactionHelp() {
        System.out.println("\n========== TRANSACTION RULES ==========");
        System.out.println("Deposits — Must be greater than zero.");
        System.out.println("Withdrawals — Cannot exceed balance unless overdraft is active.");
        System.out.println("ISA Withdrawal — Cannot go below £0. No overdraft permitted.");
        System.out.println("Business Fee — £120 annual fee applied automatically.");
        System.out.println("=======================================\n");
    }

    public static void showAuthHelp() {
        System.out.println("\n========== AUTHENTICATION ==========");
        System.out.println("Enter the customer's unique 6-digit Customer ID to authenticate.");
        System.out.println("All account operations require a customer to be authenticated first.");
        System.out.println("Type 'switch' to authenticate a different customer.");
        System.out.println("====================================\n");
    }
}