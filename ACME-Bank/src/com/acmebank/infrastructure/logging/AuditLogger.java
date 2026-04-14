package com.acmebank.infrastructure.logging;

public interface AuditLogger {
    void log(String message);
    void logError(String message, Exception exception);
    void logWarning(String warning);
    void logEvent(String eventType, String details);
    void clearLog();

}
