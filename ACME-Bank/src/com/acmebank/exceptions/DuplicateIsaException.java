package com.acmebank.exceptions;

public class DuplicateIsaException extends RuntimeException {
    public DuplicateIsaException(String message) {
        super(message);
    }
}
