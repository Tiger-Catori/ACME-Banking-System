package com.acmebank.exceptions;

public class DuplicateAccountNumberException extends RuntimeException {
    public DuplicateAccountNumberException(String message) {
        super(message);
    }
}
