package org.tx.statistics.api;

import org.hamcrest.core.IsEqual;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.tx.statistics.api.requests.TransactionRequest;
import org.tx.statistics.model.Statistics;
import org.tx.statistics.services.ComputationService;
import org.tx.statistics.services.RealtimeService;

import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class StatisticsControllerTest extends AbstractControllerTest {

    @MockBean
    private ComputationService computationService;

    @MockBean
    private RealtimeService realtimeService;

    @Test
    public void testGetStatistics() throws Exception {
        Statistics mock = new Statistics();
        mock.setMin(BigDecimal.valueOf(10));
        mock.setMax(BigDecimal.valueOf(20));
        mock.setSum(BigDecimal.valueOf(50));
        mock.setAvg(BigDecimal.valueOf(10));
        mock.setCount(10L);

        when(realtimeService.getRealtimeStatistics()).thenReturn(mock);

        mvc.perform(get("/statistics"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.max", IsEqual.equalTo("20.00")))
                .andExpect(jsonPath("$.sum", IsEqual.equalTo("50.00")))
                .andExpect(jsonPath("$.avg", IsEqual.equalTo("10.00")))
                .andExpect(jsonPath("$.count", IsEqual.equalTo(10)))
                .andExpect(jsonPath("$.min", IsEqual.equalTo("10.00")));

        verify(realtimeService).getRealtimeStatistics();
    }

    @Test
    public void testRegisterTransaction200() throws Exception {
        String timestamp = "2018-07-17T09:59:51.312Z";
        TransactionRequest request = new TransactionRequest("11.11", timestamp);

        when(computationService.computeStatistics(any())).thenReturn(UUID.randomUUID().toString());

        mvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        verify(computationService).computeStatistics(any());
    }

    @Test
    public void testRegisterTransaction204() throws Exception {
        String timestamp = "2018-07-17T09:59:51.312Z";
        TransactionRequest request = new TransactionRequest("11.11", timestamp);

        when(computationService.computeStatistics(any())).thenReturn(null);

        mvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        verify(computationService).computeStatistics(any());
    }

    @Test
    public void testClearStatistics() throws Exception {
        doNothing().when(computationService).resetStatistics();

        mvc.perform(delete("/transactions"))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        verify(computationService).resetStatistics();
    }

    @Before
    public void setup() {
        Mockito.reset(computationService);
        Mockito.reset(realtimeService);
    }
}
