package org.tx.statistics.api.requests;

import javax.validation.constraints.NotNull;

/**
 * Transaction request contains amount and timestamp of the transaction.
 */
public class TransactionRequest {

    @NotNull(message = "amount cannot be null")
    private String amount;

    @NotNull(message = "timestamp cannot be null")
    private String timestamp;

    public TransactionRequest() {
    }

    public TransactionRequest(String amount, String timestamp) {
        this.amount = amount;
        this.timestamp = timestamp;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "TransactionRequest{" +
                "amount='" + amount + '\'' +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }
}
