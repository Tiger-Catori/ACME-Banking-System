package com.acmebank.infrastructure.persistance;

import com.acmebank.infrastructure.logging.AuditLogger;
import com.acmebank.model.Customer;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JsonPersistance implements DataPersistance {
    private final String filePath;
    private final AuditLogger logger;
    private final ObjectMapper objectMapper;

    @Override
    public void saveCustomers(List<Customer> customers) {
        try {
            // Ensure the directory exists (e.g., "data/" folder)
            File file = new File(filePath);
            File parentDir = file.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                boolean created = parentDir.mkdirs();
                if (created) {
                    logger.log("Created directory: " + parentDir.getAbsolutePath());
                }
            }

            // Write the list to JSON
            objectMapper.writeValue(file, customers);
            logger.log("Successfully saved " + customers.size() + " customers to " + filePath);
        } catch (IOException e) {
            logger.logError("Failed to save customers to " + filePath, e);
            throw new RuntimeException("Unable to save customer data. Check logs for details.", e);
        }
    }


    // Loads the list of customers form teh JSON file
    // If file does not exist, return an empty list.
    @Override
    public List<Customer> loadCustomers() {
        File file = new File(filePath);
        if (!file.exists()) {
            logger.log("No existing data file found at "+ filePath + ". Starting with empty customer list.");
            return new ArrayList<>();
        }

        try {
            List<Customer> customers = objectMapper.readValue(file, new TypeReference<List<Customer>>() {});

            logger.log("Successfully loaded " + customers.size() + " customers from " + filePath);
            return customers;
        }   catch (IOException e) {
            logger.logError("Failed to load customers from " + filePath, e);
            throw new RuntimeException("Unable to load customer data. File may be corrupted. Check logs.", e);
        }
    }
}
