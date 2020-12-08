package org.tx.statistics.converters;

import org.tx.statistics.api.requests.TransactionRequest;
import org.tx.statistics.date.DateProvider;
import org.tx.statistics.errors.InvalidAmountError;
import org.tx.statistics.errors.TimestampError;
import org.tx.statistics.model.Transaction;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class TransactionConverter {

    private final DateProvider dateProvider;

    public TransactionConverter(DateProvider dateProvider) {
        this.dateProvider = dateProvider;
    }

    public Transaction toEntity(TransactionRequest request) throws InvalidAmountError, TimestampError {
        Transaction entity = new Transaction();
        entity.setAmount(convertAmount(request));
        entity.setTimestamp(dateProvider.toTimestamp(request.getTimestamp()));
        return entity;
    }

    private BigDecimal convertAmount(TransactionRequest request) throws InvalidAmountError {
        BigDecimal amount;
        try {
            amount = new BigDecimal(request.getAmount());
        } catch (Exception e) {
            throw new InvalidAmountError(e.getMessage(), e);
        }
        return amount;
    }
}
