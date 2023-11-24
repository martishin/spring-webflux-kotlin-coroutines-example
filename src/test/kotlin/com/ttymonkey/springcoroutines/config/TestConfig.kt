package com.ttymonkey.springcoroutines.config

import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.simple.SimpleMeterRegistry
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class TestConfig {
    @Bean
    fun meterRegistry(): MeterRegistry {
        return SimpleMeterRegistry()
    }
}
