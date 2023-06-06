package com.framework.config;

import com.github.rholder.retry.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class RetryingConfig {

    private static final Logger logger = LoggerFactory.getLogger(RetryingConfig.class);

    @Bean
    public Retryer<Boolean> retryer() {
        logger.info("Retryer 初始化开始...");
        Retryer<Boolean> retryer = RetryerBuilder
                .<Boolean>newBuilder()
                .retryIfExceptionOfType(Exception.class)
                .retryIfResult(bool -> !bool)
                .withWaitStrategy(WaitStrategies.fixedWait(3, TimeUnit.SECONDS))
                .withStopStrategy(StopStrategies.stopAfterAttempt(3))
                .withAttemptTimeLimiter(AttemptTimeLimiters.fixedTimeLimit(3, TimeUnit.SECONDS))
                .build();

        return retryer;
    }

}
