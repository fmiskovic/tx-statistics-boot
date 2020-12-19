package org.tx.statistics.realtime;

import org.tx.statistics.model.Statistics;

import java.time.Instant;
import java.util.TreeMap;

/**
 * Holds realtime statistics map. By default, it is 60 entries for every second in the last minute.
 */
public class StatisticsHolder {

    public static volatile TreeMap<Instant, Statistics> REALTIME = new TreeMap<>();
}
