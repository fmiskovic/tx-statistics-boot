package org.tx.statistics.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.tx.statistics.config.StatisticsConfiguration;
import org.tx.statistics.date.DateProvider;
import org.tx.statistics.model.Statistics;
import org.tx.statistics.realtime.StatisticsHolder;

import java.time.Instant;

@Service
public class RealtimeServiceImpl implements RealtimeService {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final StatisticsConfiguration config;

    private final DateProvider dateProvider;

    public RealtimeServiceImpl(DateProvider dateProvider, StatisticsConfiguration config) {
        this.dateProvider = dateProvider;
        this.config = config;
    }

    @Override
    public Statistics getRealtimeStatistics() {
        log.debug("Getting realtime statistics...");
        Instant now = dateProvider.now();
        evictOutdated(now);
        return StatisticsHolder.REALTIME.isEmpty() ? new Statistics() : StatisticsHolder.REALTIME.firstEntry().getValue();
    }

    // evict old entries
    private void evictOutdated(Instant now) {
        log.debug("Evicting old entries from realtime statistics...");
        Instant start = now.minusMillis(config.getDuration());
        StatisticsHolder.REALTIME.keySet().removeIf(key -> key.isBefore(start));
    }
}
