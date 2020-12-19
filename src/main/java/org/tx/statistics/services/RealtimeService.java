package org.tx.statistics.services;

import org.tx.statistics.model.Statistics;

@FunctionalInterface
public interface RealtimeService {

    /**
     * Get realtime statistics.
     *
     * @return {@link Statistics}
     */
    Statistics getRealtimeStatistics();
}
