package org.tx.statistics.validation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.tx.statistics.errors.TimestampError;

import java.time.Instant;

/**
 * Responsible for transaction validation. All rules should be implemented here.
 */
@Component
public class TransactionValidator {

    private final Logger log = LoggerFactory.getLogger(getClass());

    /**
     * Validate transaction timestamp.
     *
     * @param duration - time interval of the statistics
     * @param timestamp - time of the transaction
     * @param now - current time
     * @return true if transaction is in specified time interval.
     * @throws TimestampError
     */
    public boolean isValid(long duration, Instant timestamp, Instant now) throws TimestampError {
        log.debug("Validating transaction timestamp {} against {}", timestamp, now);
        if (timestamp.isAfter(now)) {
            throw new TimestampError("Impossible request because transaction timestamp is in the future.");
        }
        log.debug("Timestamp is not in the future and that is expected.");

        Instant start = now.minusMillis(duration);
        return timestamp.isAfter(start);
    }
}
