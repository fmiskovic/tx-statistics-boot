package org.tx.statistics.date;

import org.tx.statistics.errors.TimestampError;

import java.time.Instant;

/**
 * Provides dates and handles transaction timestamps in the ISO-8601 basic local date format.
 */
public interface DateProvider {

    Instant toTimestamp(String timestamp) throws TimestampError;

    String fromTimestamp(Instant timestamp) throws TimestampError;

    default Instant now() {
        return Instant.now();
    }

    default long nowMillis() {
        return now().toEpochMilli();
    }
}
