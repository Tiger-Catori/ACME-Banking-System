package com.acmebank.service;
import com.acmebank.model.enums.BusinessType;
import com.acmebank.exceptions.InvalidBusinessTypeException;

public class BusinessAccountValidator {

    private static final String[] rejectedNameTypes = {
            "Enterprise", "Public Limited Company (PLC)", "Charity", "Public Sector Organisation"
    };

    private String acceptedTypesMessage() {
        return "Accepted types are: Sole Trader, Partnership, Limited Company.";
    }

    public void validate(BusinessType businessType) throws InvalidBusinessTypeException {
        validateBusinessType(businessType);
    }

    private void validateBusinessType(BusinessType businessType) throws InvalidBusinessTypeException {
        if (businessType == null) {
            throw new InvalidBusinessTypeException(
                    "A valid business type must be provided. " + acceptedTypesMessage()
            );
        }
    }

    public void validateDataInput(String dataInput) throws InvalidBusinessTypeException {
        if (dataInput.isBlank()) {
            throw new InvalidBusinessTypeException("Enter Business Type " + acceptedTypesMessage());
        }

        String protectData = validateRejectedTypes(dataInput);

        try {
            BusinessType.valueOf(protectData.toUpperCase().replace(" ", "_"));
        } catch (IllegalArgumentException e) {
            throw new InvalidBusinessTypeException(
                    "'" + dataInput.trim() + "' is not a recognised business type. " + acceptedTypesMessage()
            );
        }
    }

    private static String validateRejectedTypes(String dataInput) throws InvalidBusinessTypeException {
        String protectData = dataInput.trim().toLowerCase();

        for (String rejected : rejectedNameTypes) {
            if (protectData.equals(rejected.toLowerCase())) {
                throw new InvalidBusinessTypeException(
                        "'" + dataInput.trim() + "' is not an eligible business type for an ACME Bank Business Account. " +
                                "ACME Bank does not support: Enterprise, Public Limited Company (PLC), Charity or Public Sector accounts.");
            }
        }
        return protectData;
    }
}


