package org.tx.statistics.services;

import org.tx.statistics.api.requests.TransactionRequest;
import org.tx.statistics.errors.InvalidAmountError;
import org.tx.statistics.errors.TimestampError;

/**
 * Compute and reset realtime statistics.
 */
public interface ComputationService {

    /**
     * Computes statistics if transaction request is valid.
     *
     * @param request - {@link TransactionRequest} transaction request.
     * @return - UUID if transaction timestamp is valid or null if not.
     * @throws InvalidAmountError - throws when transaction amount is invalid.
     * @throws TimestampError     - throws when timestamp is in the future.
     */
    String computeStatistics(TransactionRequest request) throws InvalidAmountError, TimestampError;

    /**
     * Reset statistics.
     */
    void resetStatistics();
}
