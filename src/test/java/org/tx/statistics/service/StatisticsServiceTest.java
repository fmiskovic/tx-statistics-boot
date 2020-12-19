package org.tx.statistics.service;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.tx.statistics.api.requests.TransactionRequest;
import org.tx.statistics.date.DateProvider;
import org.tx.statistics.errors.InvalidAmountError;
import org.tx.statistics.errors.TimestampError;
import org.tx.statistics.model.Statistics;
import org.tx.statistics.services.ComputationService;
import org.tx.statistics.services.RealtimeService;

import java.time.Instant;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class StatisticsServiceTest extends AbstractServiceTest {

    @Autowired
    private ComputationService computationService;

    @Autowired
    private DateProvider dateProvider;

    @Autowired
    private RealtimeService realtimeService;

    @Before
    public void cleanup() {
        computationService.resetStatistics();
    }

    @Test
    public void testRegisterTransaction() throws InvalidAmountError, TimestampError {
        String amount = "12.33";
        String timestamp = dateProvider.fromTimestamp(Instant.now().minusSeconds(3));

        TransactionRequest request = new TransactionRequest();
        request.setAmount(amount);
        request.setTimestamp(timestamp);
        String id = computationService.computeStatistics(request);
        assertNotNull(id);

        Statistics statistics = realtimeService.getRealtimeStatistics();
        assertEquals(12.33, statistics.getAvg().doubleValue(), 0.00);
        assertEquals(12.33, statistics.getSum().doubleValue(), 0.00);
        assertEquals(12.33, statistics.getMax().doubleValue(), 0.00);
        assertEquals(12.33, statistics.getMin().doubleValue(), 0.00);
        assertEquals(1, statistics.getCount().intValue());
    }

    @Test
    public void testResetStatistics() throws InvalidAmountError, TimestampError {
        String amount = "12.33";
        String timestamp = dateProvider.fromTimestamp(Instant.now().minusSeconds(3));

        TransactionRequest request = new TransactionRequest();
        request.setAmount(amount);
        request.setTimestamp(timestamp);
        String id = computationService.computeStatistics(request);
        assertNotNull(id);

        Statistics statistics = realtimeService.getRealtimeStatistics();
        assertEquals(12.33, statistics.getAvg().doubleValue(), 0.0);
        assertEquals(12.33, statistics.getSum().doubleValue(), 0.0);
        assertEquals(12.33, statistics.getMax().doubleValue(), 0.0);
        assertEquals(12.33, statistics.getMin().doubleValue(), 0.0);
        assertEquals(1, statistics.getCount().intValue());

        computationService.resetStatistics();

        statistics = realtimeService.getRealtimeStatistics();
        assertEquals(00.00, statistics.getAvg().doubleValue(), 0.0);
        assertEquals(00.00, statistics.getSum().doubleValue(), 0.0);
        assertEquals(0, statistics.getCount().intValue());
    }

    @Test(expected = InvalidAmountError.class)
    public void testRegisterTransactionWithInvalidAmount() throws InvalidAmountError, TimestampError {
        String amount = "12.krf";
        String timestamp = dateProvider.fromTimestamp(Instant.now().minusSeconds(3));

        TransactionRequest request = new TransactionRequest();
        request.setAmount(amount);
        request.setTimestamp(timestamp);
        computationService.computeStatistics(request);
    }

    @Test(expected = TimestampError.class)
    public void testRegisterTransactionWithInvalidTimestamp() throws InvalidAmountError, TimestampError {
        String amount = "12.000";
        String timestamp = "2018-07-17 PM 09:59:51.312F";

        TransactionRequest request = new TransactionRequest();
        request.setAmount(amount);
        request.setTimestamp(timestamp);
        computationService.computeStatistics(request);
    }

    @Test(expected = TimestampError.class)
    public void testRegisterTransactionWithFutureTimestamp() throws InvalidAmountError, TimestampError {
        String amount = "12.000";
        String timestamp = dateProvider.fromTimestamp(Instant.now().plusSeconds(5));

        TransactionRequest request = new TransactionRequest();
        request.setAmount(amount);
        request.setTimestamp(timestamp);
        computationService.computeStatistics(request);
    }

    @Test
    public void testCalculateStatistics() throws TimestampError, InvalidAmountError {
        String timestamp1 = dateProvider.fromTimestamp(Instant.now().minusSeconds(2));
        String timestamp2 = dateProvider.fromTimestamp(Instant.now().minusSeconds(3));
        String timestamp3 = dateProvider.fromTimestamp(Instant.now().minusSeconds(4));
        String timestamp4 = dateProvider.fromTimestamp(Instant.now().minusSeconds(69));

        computationService.computeStatistics(new TransactionRequest("12.00", timestamp1));
        computationService.computeStatistics(new TransactionRequest("13.00", timestamp2));
        computationService.computeStatistics(new TransactionRequest("14.00", timestamp3));
        computationService.computeStatistics(new TransactionRequest("15.00", timestamp4));

        Statistics statistics = realtimeService.getRealtimeStatistics();
        assertEquals(13.00, statistics.getAvg().doubleValue(), 0.0);
        assertEquals(39.00, statistics.getSum().doubleValue(), 0.0);
        assertEquals(14.00, statistics.getMax().doubleValue(), 0.0);
        assertEquals(12.00, statistics.getMin().doubleValue(), 0.0);
        assertEquals(3, statistics.getCount().intValue());
    }
}
