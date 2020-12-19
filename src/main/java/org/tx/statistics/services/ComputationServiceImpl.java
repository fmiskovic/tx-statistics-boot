package org.tx.statistics.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.tx.statistics.api.requests.TransactionRequest;
import org.tx.statistics.config.StatisticsConfiguration;
import org.tx.statistics.converters.TransactionConverter;
import org.tx.statistics.date.DateProvider;
import org.tx.statistics.errors.InvalidAmountError;
import org.tx.statistics.errors.TimestampError;
import org.tx.statistics.model.Statistics;
import org.tx.statistics.model.Transaction;
import org.tx.statistics.realtime.StatisticsHolder;
import org.tx.statistics.validation.TransactionValidator;

import java.time.Instant;
import java.util.UUID;

@Service
public class ComputationServiceImpl implements ComputationService {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final StatisticsConfiguration config;

    private final TransactionConverter converter;

    private final DateProvider dateProvider;

    private final TransactionValidator validator;

    public ComputationServiceImpl(StatisticsConfiguration config,
                                  DateProvider dateProvider,
                                  TransactionConverter converter,
                                  TransactionValidator validator) {
        this.config = config;
        this.dateProvider = dateProvider;
        this.converter = converter;
        this.validator = validator;
    }

    @Override
    public String computeStatistics(TransactionRequest request) throws InvalidAmountError, TimestampError {
        log.debug("Creating new transaction...");
        Transaction transaction = converter.toEntity(request);
        Instant now = dateProvider.now();

        boolean valid = validator.isValid(config.getDuration(), transaction.getTimestamp(), now);
        if (!valid) {
            return null;
        }

        // keep realtime statistics up to date and evict old entries
        refreshRealTimeStatistics(now);

        StatisticsHolder.REALTIME.forEach((time, statistics) -> {
            if (transaction.getTimestamp().isAfter(time)) {
                statistics.calculate(transaction.getAmount());
            }
        });

        return UUID.randomUUID().toString();
    }

    @Override
    public void resetStatistics() {
        StatisticsHolder.REALTIME.clear();
    }

    // create new time entries and evict old ones
    private void refreshRealTimeStatistics(Instant now) {
        log.debug("Refreshing realtime statistics...");
        long interval = config.getInterval();
        long duration = config.getDuration();

        Instant start = now.minusMillis(duration);
        StatisticsHolder.REALTIME.computeIfAbsent(start, k -> new Statistics());

        for (long j = duration; j > 0; j = j - interval) {
            StatisticsHolder.REALTIME.computeIfAbsent(now.minusMillis(j), k -> new Statistics());
        }

        evictOutdated(now);
    }

    // evict old entries
    private void evictOutdated(Instant now) {
        log.debug("Evicting old entries from realtime statistics...");
        Instant start = now.minusMillis(config.getDuration());
        StatisticsHolder.REALTIME.keySet().removeIf(key -> key.isBefore(start));
    }
}
