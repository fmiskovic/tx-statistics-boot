package org.tx.statistics.date;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DefaultDateConfig {

    @Bean
    @ConditionalOnMissingBean
    public DateProvider dateProvider() {
        return new DefaultDateProvider();
    }
}
