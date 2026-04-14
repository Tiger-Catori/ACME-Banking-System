package com.acmebank.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Objects;

// Immutable value object representing an 8-digit account number.
public final class AccountNumber {
    private final String value;

    @JsonCreator
    public AccountNumber(@JsonProperty String value) {
        if (value == null) {
            throw new IllegalArgumentException("An account number cannot be null");
        }
        // Checking the number of digits.
        if (value.length() != 8) {
            throw new IllegalArgumentException("An account number has to have 8 digits.");
        }

        // Checking it only has digits 0-9
        if (!value.matches("\\d+")) {
            throw new IllegalArgumentException("Account number must only contain digits 0-9.");
        }

        this.value = value;
    }

    public static AccountNumber create(String value) {
        return new AccountNumber(value);
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public boolean equals(Object obj) {
        // same object
        if (this == obj) return true;

        // if object is not null and is the same type as this.
        // proceed to cast
        if (obj == null || getClass() != obj.getClass()) return true;
        AccountNumber that = (AccountNumber) obj;

        // Compares actual value of AccountNumber Objects
        return Objects.equals(value, that.value);
    }

    @Override
    public String toString() {
        return getValue();
    }
}