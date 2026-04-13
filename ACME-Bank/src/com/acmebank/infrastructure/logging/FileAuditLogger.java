package com.acmebank.infrastructure.logging;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FileAuditLogger implements AuditLogger {
    private final String filePath;
    private final DateTimeFormatter formatter;
    // Constructor
    public FileAuditLogger(String filePath) {
        if (filePath == null || filePath.trim().isEmpty()) {
            throw new IllegalArgumentException("Log file path cannot be full or empty.");
        }
        // Defining the format
        this.formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.filePath = filePath;
    }

    // Helper function to write a formatted log line to the file.
    private void writeToFile(String formattedLine) {
        try(
                FileWriter fileWriter = new FileWriter(filePath, true);
                PrintWriter printWriter = new PrintWriter(fileWriter)
        ) {     printWriter.println(formattedLine);     }
        catch (IOException e) {
            // If logging fails
            System.err.println("Failed to write to log file " + filePath);
            e.printStackTrace(System.err);
        }

    }

    // Generate timestamp in format [yyyy-MM-dd HH:mm:ss]
    private String getTimestamp() {
        String timestampString = "[" + LocalDateTime.now().format(formatter) + "]";
        return timestampString;

    }

    @Override
    public void log(String message) {
        String logLine = getTimestamp() + " INFO: " + message;
        writeToFile(logLine);
    }

    @Override
    public void logError(String message, Exception exception) {
        // Error message & exception details
        String errorDetail = message + " - " + exception.toString();
        String logLine = getTimestamp() + " ERROR: " + errorDetail;
        writeToFile(logLine);
    }

    @Override
    public void logWarning(String warning) {
        String logLine = getTimestamp() + " WARNING: " + warning;
        writeToFile(logLine);
    }

    @Override
    public void logEvent(String eventType, String details) {
        String logLine = getTimestamp() + " EVENT [" + eventType + "]: " + details;
        writeToFile(logLine);
    }

    public void clearLog() {
        try (FileWriter fw = new FileWriter(filePath, false)) {
            // false = overwrite mode
            fw.write("");
        } catch (IOException e) {
            System.err.println("Failed to clear log file: " + filePath);
            e.printStackTrace();
        }
    }
}
