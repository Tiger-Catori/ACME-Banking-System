package com.acmebank.cli.handlers;

import java.util.Scanner;

public class InputHandler {
     private final Scanner scanner;

    // Constructor
    public InputHandler() {
        this.scanner = new Scanner(System.in);
    }

    // Reading raw string line
    public String readLine(String prompt) {
        System.out.println(prompt);
        return scanner.nextLine().trim();
    }

    // Reads an int (loops until valid)
    public int readInt(String prompt) {
        while (true) {
            try {
                System.out.println(prompt);
                int value = Integer.parseInt(scanner.nextLine().trim());
                return value;
            }   catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a whole number.");
            }
        }
    }

    // Reads a double (loops until valid)
    public double readDouble(String prompt) {
        while (true) {
            try {
                System.out.println(prompt);
                double value = Double.parseDouble(scanner.nextLine().trim());
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    // Read a choice from a set of valid options (case-insensitive)
    public String readChoice(String prompt, String... validOptions) {
        while (true) {
            String input = readLine(prompt);
            for (String option : validOptions) {
                if (option.equalsIgnoreCase(input)) {
                    return option;
                }
            }
            System.out.println("Invalid choice. Please enter one of: " + String.join(", ", validOptions));
        }
    }

    // Read a yes/no answer (returns true for yes, false for no)
    public boolean readYesNo(String prompt) {
        String input = readChoice(prompt, "y", "n", "yes", "no");
        return input.equalsIgnoreCase("y") || input.equalsIgnoreCase("yes");
    }

    // Closing the scanner (call when application exits)
    public void close() {
        scanner.close();
    }
}
