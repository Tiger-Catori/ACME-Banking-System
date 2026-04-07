package com.acmebank.model.enums;

public enum BusinessType {
    // Business types for BusinessAccount eligibility.
    // Eligible types: SOLE_TRADER, LTD_COMPANY, PARTNERSHIP
    // Excluded types: ENTERPRISE, PLC, CHARITY, PUBLIC_SECTOR

    // Allowed types
    SOLE_TRADER(true, "Sole Trader"),
    LTD_COMPANY(true, "Limited Company"),
    PARTNERSHIP(true, "Partnership"),

    // Not allowed Types
    CHARITY(false, "Charity"),
    PLC(false, "Public Limited Company (PLC)"),
    ENTERPRISE(false, "Enterprise"),
    PUBLIC_SECTOR(false, "Public Sector Organisation");

    private final boolean eligible;
    private final String displayName;

    BusinessType(boolean eligible, String displayName) {
        this.eligible = eligible;
        this.displayName = displayName;
    }

    public boolean isEligible() {
        return eligible;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return getDisplayName();
    }
}
