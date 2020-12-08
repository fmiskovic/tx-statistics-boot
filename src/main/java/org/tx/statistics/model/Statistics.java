package org.tx.statistics.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * Entity class that holds the information about transaction statistics.
 */
public class Statistics {

    private BigDecimal avg;

    private Long count;

    private BigDecimal max;

    private BigDecimal min;

    private BigDecimal sum;

    public Statistics() {
        this.count = 0L;
        this.avg = BigDecimal.valueOf(00.00).setScale(2, RoundingMode.HALF_EVEN);
        this.sum = BigDecimal.valueOf(00.00).setScale(2, RoundingMode.HALF_EVEN);
    }

    public BigDecimal getSum() {
        return sum;
    }

    public void setSum(BigDecimal sum) {
        this.sum = sum;
    }

    public BigDecimal getAvg() {
        return avg;
    }

    public void setAvg(BigDecimal avg) {
        this.avg = avg;
    }

    public BigDecimal getMax() {
        return max;
    }

    public void setMax(BigDecimal max) {
        this.max = max;
    }

    public BigDecimal getMin() {
        return min;
    }

    public void setMin(BigDecimal min) {
        this.min = min;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public void calculate(BigDecimal amount) {
        if (Objects.isNull(this.min) || amount.compareTo(this.min) < 0) {
            this.min = amount;
        }
        if (Objects.isNull(max) || amount.compareTo(this.max) > 0) {
            this.max = amount;
        }
        this.count++;
        this.sum = this.sum.add(amount);
        this.avg = this.sum.divide(new BigDecimal(this.count), 2, RoundingMode.HALF_EVEN);
    }
}
