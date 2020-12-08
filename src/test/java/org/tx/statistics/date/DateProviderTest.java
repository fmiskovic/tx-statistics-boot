package org.tx.statistics.date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.tx.statistics.errors.TimestampError;

import java.time.Instant;

import static org.junit.Assert.assertEquals;

@SpringBootTest
@RunWith(SpringRunner.class)
public class DateProviderTest {

    @Autowired
    private DateProvider dateProvider;

    @Test
    public void testToTimeStamp() throws TimestampError {
        String givenTime = "2018-07-17T09:59:51.312Z";

        Instant localDateTime = dateProvider.toTimestamp(givenTime);
        assertEquals(givenTime, localDateTime.toString());
    }

    @Test
    public void testFromTimeStamp() throws TimestampError {
        String givenTime = "2018-07-17T09:59:51.312Z";

        Instant localDateTime = dateProvider.toTimestamp(givenTime);
        assertEquals(givenTime, localDateTime.toString());

        String expectedTime = dateProvider.fromTimestamp(localDateTime);
        assertEquals(givenTime, expectedTime);
    }

    @Test(expected = TimestampError.class)
    public void testInvalidTimeStamp() throws TimestampError {
        String givenTime = "2018-07-17T09:59:51.312K";

        Instant localDateTime = dateProvider.toTimestamp(givenTime);
        assertEquals(givenTime, localDateTime.toString());
    }
}
