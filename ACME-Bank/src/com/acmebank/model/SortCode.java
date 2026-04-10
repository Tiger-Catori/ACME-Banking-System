package com.acmebank.model;

// Immutable value object representing a UK sort code
// In the format XX-XX-XX

import java.lang.management.ThreadInfo;
import java.util.Objects;

public final class SortCode {
    private final int part1;
    private final int part2;
    private final int part3;

    public SortCode (int part1, int part2, int part3) {
        validatePart(part1);
        validatePart(part2);
        validatePart(part3);
        this.part1 = part1;
        this.part2 = part2;
        this.part3 = part3;
    }

    public void validatePart(int part) {
        if (part < 0 || part > 99) {
            throw new IllegalArgumentException("Sort code must be between 0 an 99.");
        }
    }

    // Factory Method form 3 integers.
    public static SortCode from(int part1, int part2, int part3) {
        return new SortCode(part1, part2, part3);
    }

    // Factory method from a single Integer
    public static SortCode fromInt(int sortCode) {
        if (sortCode < 0 || sortCode > 999999) {
            throw new IllegalArgumentException("SortCode integer must be between 0 and 999999.");
        }

        try {
            int part1 = sortCode % 100;
            int part2 = (sortCode / 100) % 100;
            int part3 = (sortCode / 10000) % 100;

            return new SortCode(part1, part2, part3);

        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Sort code part must be numbers.");
        }
    }

    // Factory method for formatted string "XX-XX-XX"
    public static SortCode fromString(String sortCode) {
        String [] parts = sortCode.split("-");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Sortcode string must have 3 parts.");
        }

        int part1 = Integer.parseInt(parts[0]);
        int part2 = Integer.parseInt(parts[1]);
        int part3 = Integer.parseInt(parts[2]);

        return new SortCode(part1, part2, part3);
    }

    public int getPart1() {
        return part1;
    }

    public int getPart2() {
        return part2;
    }

    public int getPart3() {
        return part3;
    }

    public int toInt() {
        return getPart1() * 10000 + getPart2() * 100 + getPart3();
    }

    @Override
    public String toString() {
        return String.format("%02d-%02d-%02d", getPart1(), getPart2(), getPart3());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;

        if (obj == null || getClass() != obj.getClass()) return false;
        SortCode sortCode = (SortCode) obj;

        return part1 == sortCode.part1 && part2 == sortCode.part2 && part3 == sortCode.part3;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPart1(), getPart2(), getPart3());
    }
}