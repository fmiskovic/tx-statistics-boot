package org.tx.statistics.date;

import org.tx.statistics.errors.TimestampError;

import java.time.Instant;

public class DefaultDateProvider implements DateProvider {

    public Instant toTimestamp(String timestamp) throws TimestampError {
        Instant time;
        try {
            time = Instant.parse(timestamp);
        } catch (Exception e) {
            throw new TimestampError("Invalid timestamp format. Supported is ISO 8601.", e);
        }
        return time;
    }

    public String fromTimestamp(Instant timestamp) throws TimestampError {
        String time;
        try {
            time = timestamp.toString();
        } catch (Exception e) {
            throw new TimestampError("Invalid timestamp format. Supported is ISO 8601.", e);
        }
        return time;
    }
}
