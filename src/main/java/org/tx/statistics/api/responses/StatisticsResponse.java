package org.tx.statistics.api.responses;

/**
 * Statistics response:
 * ● sum – a BigDecimal specifying the total sum of transaction value
 * ● avg – a BigDecimal specifying the average amount of transaction value
 * ● max – a BigDecimal specifying single highest transaction value
 * ● min – a BigDecimal specifying single lowest transaction value
 * ● count – a long specifying the total number of transactions
 */
public class StatisticsResponse {

    private String avg;

    private Long count;

    private String max;

    private String min;

    private String sum;

    public String getSum() {
        return sum;
    }

    public void setSum(String sum) {
        this.sum = sum;
    }

    public String getAvg() {
        return avg;
    }

    public void setAvg(String avg) {
        this.avg = avg;
    }

    public String getMax() {
        return max;
    }

    public void setMax(String max) {
        this.max = max;
    }

    public String getMin() {
        return min;
    }

    public void setMin(String min) {
        this.min = min;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "StatisticsResponse{" +
                "sum='" + sum + '\'' +
                ", avg='" + avg + '\'' +
                ", max='" + max + '\'' +
                ", min='" + min + '\'' +
                ", count=" + count +
                '}';
    }
}
