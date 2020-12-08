package org.tx.statistics.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.tx.statistics.api.requests.TransactionRequest;
import org.tx.statistics.api.responses.StatisticsResponse;
import org.tx.statistics.converters.StatisticsConverter;
import org.tx.statistics.errors.InvalidAmountError;
import org.tx.statistics.errors.TimestampError;
import org.tx.statistics.services.StatisticsService;

import javax.validation.Valid;
import java.net.URI;
import java.util.Objects;

@RestController
public class StatisticsController {

    private final StatisticsConverter converter;

    private final StatisticsService service;

    public StatisticsController(StatisticsService service, StatisticsConverter converter) {
        this.service = service;
        this.converter = converter;
    }

    /**
     * @param request - {@link TransactionRequest}
     * @return Empty body with one of the following:
     * ● 201 – in case of success
     * ● 204 – if the transaction is older than 60 seconds (by default)
     * ● 400 – if the JSON is invalid
     * ● 422 – if any of the fields are not parsable or the transaction date is in the future
     * @throws InvalidAmountError
     * @throws TimestampError
     */
    @PostMapping("/transactions")
    public ResponseEntity<URI> registerTransaction(@Valid @RequestBody TransactionRequest request) throws InvalidAmountError, TimestampError {
        String id = service.registerTransaction(request);

        // Send location in response if id is not null
        if (Objects.nonNull(id)) {
            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(id)
                    .toUri();
            return ResponseEntity.created(location).build();
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    /**
     * Reset/Clear real-time statistics.
     *
     * @return - Empty body with 204.
     */
    @DeleteMapping("/transactions")
    public ResponseEntity<Void> clearStatistics() {
        service.resetStatistics();
        return ResponseEntity.noContent().build();
    }

    /**
     * Get real-time statistics.
     *
     * @return {@link StatisticsResponse}
     */
    @GetMapping("/statistics")
    public StatisticsResponse getStatistics() {
        return converter.toResponse(service.getStatistics());
    }
}
