package com.example.demo.config;

import com.example.demo.service.EarlyBirdDiscountService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class DiscountFeatureConfig {

    @Bean
    @ConditionalOnProperty(name = "feature.earlybird.enabled", havingValue = "true")
    public EarlyBirdDiscountService earlyBirdDiscountService() {
        return new EarlyBirdDiscountService();
    }
}
