package org.tx.statistics.converters;

import org.tx.statistics.api.responses.StatisticsResponse;
import org.tx.statistics.model.Statistics;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class StatisticsConverter {

    public StatisticsResponse toResponse(Statistics statistics) {
        StatisticsResponse response = new StatisticsResponse();

        if (Objects.isNull(statistics)) {
            response.setAvg("0.00");
            response.setSum("0.00");
            response.setMax("0.00");
            response.setMin("0.00");
            response.setCount(0L);
            return response;
        }

        String avg = String.valueOf(statistics.getAvg());
        String max = Objects.nonNull(statistics.getMax()) ? String.valueOf(statistics.getMax()) : "0.00";
        String min = Objects.nonNull(statistics.getMin()) ? String.valueOf(statistics.getMin()) : "0.00";
        String sum = String.valueOf(statistics.getSum());

        String[] arrAvg = avg.split("\\.");
        avg = parseString(avg, arrAvg);
        String[] arrSum = sum.split("\\.");
        sum = parseString(sum, arrSum);
        String[] arrMin = min.split("\\.");
        min = parseString(min, arrMin);
        String[] arrMax = max.split("\\.");
        max = parseString(max, arrMax);

        response.setAvg(avg);
        response.setSum(sum);
        response.setMax(max);
        response.setMin(min);
        response.setCount(statistics.getCount());

        return response;
    }

    private String parseString(String str, String[] arr) {
        if (arr.length < 2) {
            if (Objects.nonNull(str)) {
                str = str.concat(".00");
            } else {
                str = "0.00";
            }
        }
        return str;
    }
}
