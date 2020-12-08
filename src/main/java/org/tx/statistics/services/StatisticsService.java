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
import org.tx.statistics.validation.TransactionValidator;

import java.time.Instant;
import java.util.TreeMap;
import java.util.UUID;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * This service registers transactions and calculate its statistics.
 * It is also responsible for reading statistics and keeping it up to date.
 */
@Service
public class StatisticsService {

    private final StatisticsConfiguration config;

    private final TransactionConverter converter;

    private final DateProvider dateProvider;

    private final ReadWriteLock lock;

    private final Logger log = LoggerFactory.getLogger(getClass());

    // realtime statistics that holds constant number of entries
    private final TreeMap<Instant, Statistics> realTime;

    private final TransactionValidator validator;

    public StatisticsService(TransactionConverter converter,
                             TransactionValidator validator,
                             DateProvider dateProvider,
                             StatisticsConfiguration config) {
        this.converter = converter;
        this.validator = validator;
        this.dateProvider = dateProvider;
        this.config = config;
        this.realTime = new TreeMap<>();
        this.lock = new ReentrantReadWriteLock();
    }

    /**
     * Get transaction statistics.
     *
     * @return {@link Statistics}
     */
    public Statistics getStatistics() {
        try {
            lock.readLock().lock();
            log.debug("Getting statistics...");
            Instant now = dateProvider.now();

            evictOutdated(now);
            return realTime.isEmpty() ? new Statistics() : realTime.firstEntry().getValue();
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Validate transaction and calculates statistics.
     *
     * @param request - transaction request.
     * @return - UUID if transaction timestamp is valid or null if not.
     * @throws InvalidAmountError - throws when transaction amount is invalid.
     * @throws TimestampError     - throws when timestamp is in the future.
     */
    public String registerTransaction(TransactionRequest request) throws InvalidAmountError, TimestampError {
        try {
            lock.writeLock().lock();
            log.debug("Creating new transaction...");
            Transaction transaction = converter.toEntity(request);
            Instant now = dateProvider.now();
            return calculateStatistics(now, transaction);
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Reset transaction statistics.
     */
    public void resetStatistics() {
        try {
            lock.writeLock().lock();
            log.debug("Clear statistics...");
            realTime.clear();
        } finally {
            lock.writeLock().unlock();
        }
    }

    // calculate statistics if transaction is valid
    private String calculateStatistics(Instant now, Transaction transaction) throws TimestampError {
        boolean valid = validator.isValid(config.getDuration(), transaction.getTimestamp(), now);
        if (!valid) {
            return null;
        }

        // keep realtime statistics up to date and evict old entries
        refreshRealTime(now);

        realTime.forEach((time, statistics) -> {
            if (transaction.getTimestamp().isAfter(time)) {
                statistics.calculate(transaction.getAmount());
            }
        });

        return UUID.randomUUID().toString();
    }

    // compute new time entries and evict old ones
    private void refreshRealTime(Instant now) {
        long interval = config.getInterval();
        long duration = config.getDuration();

        Instant start = now.minusMillis(duration);
        realTime.computeIfAbsent(start, k -> new Statistics());

        for (long j = duration; j > 0; j = j - interval) {
            realTime.computeIfAbsent(now.minusMillis(j), k -> new Statistics());
        }

        evictOutdated(now);
    }

    // evict old entries
    private void evictOutdated(Instant now) {
        Instant start = now.minusMillis(config.getDuration());
        realTime.keySet().removeIf(key -> key.isBefore(start));
    }
}
